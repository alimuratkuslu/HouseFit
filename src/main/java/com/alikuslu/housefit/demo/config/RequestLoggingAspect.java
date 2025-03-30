package com.alikuslu.housefit.demo.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class RequestLoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingAspect.class);
    private final MeterRegistry meterRegistry;
    private final ObjectMapper objectMapper;

    @Autowired
    public RequestLoggingAspect(MeterRegistry meterRegistry, ObjectMapper objectMapper) {
        this.meterRegistry = meterRegistry;
        this.objectMapper = objectMapper;
    }

    @Around("execution(* com.alikuslu.housefit.demo.controller..*(..)) && (@annotation(org.springframework.web.bind.annotation.GetMapping) || @annotation(org.springframework.web.bind.annotation.PostMapping) || @annotation(org.springframework.web.bind.annotation.PutMapping) || @annotation(org.springframework.web.bind.annotation.DeleteMapping) || @annotation(org.springframework.web.bind.annotation.PatchMapping))")
    public Object logRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                        .getRequest();

        String correlationId = UUID.randomUUID().toString();
        MDC.put("correlationId", correlationId);

        long startTime = System.currentTimeMillis();
        String requestURI = request.getRequestURI();

        String token = request.getHeader("Authorization");
        String phoneNumber = extractPhoneNumber(token);

        Map<String, Object> requestLog = new HashMap<>();
        requestLog.put("event", "request_started");
        requestLog.put("correlationId", correlationId);
        requestLog.put("uri", requestURI);
        requestLog.put("method", request.getMethod());
        requestLog.put("params", request.getQueryString());
        requestLog.put("clientIp", request.getRemoteAddr());
        requestLog.put("userAgent", request.getHeader("User-Agent"));
        requestLog.put("phoneNumber", phoneNumber);

        logger.info("API Request: {}", objectMapper.writeValueAsString(requestLog));

        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;

            Map<String, Object> responseLog = new HashMap<>();
            responseLog.put("event", "request_completed");
            responseLog.put("correlationId", correlationId);
            responseLog.put("uri", requestURI);
            responseLog.put("status", "SUCCESS");
            responseLog.put("duration", duration);
            responseLog.put("phoneNumber", phoneNumber);

            logger.info("API Response: {}", objectMapper.writeValueAsString(responseLog));

            recordMetrics(request, duration, "200");

            return result;

        } catch (Exception ex) {
            long duration = System.currentTimeMillis() - startTime;

            Map<String, Object> errorLog = new HashMap<>();
            errorLog.put("event", "request_failed");
            errorLog.put("correlationId", correlationId);
            errorLog.put("uri", requestURI);
            errorLog.put("error", ex.getMessage());
            errorLog.put("errorType", ex.getClass().getName());
            errorLog.put("duration", duration);
            errorLog.put("phoneNumber", phoneNumber);

            logger.error("API Error: {}", objectMapper.writeValueAsString(errorLog));

            recordMetrics(request, duration, "500");

            throw ex;
        } finally {
            MDC.clear();
        }
    }

    private void recordMetrics(HttpServletRequest request, long duration, String status) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        String normalizedURI = normalizeURI(requestURI);

        meterRegistry.counter("http_requests_total",
                        "method", method,
                        "uri", normalizedURI,
                        "status", status)
                .increment();

        meterRegistry.timer("http_request_duration_seconds",
                        "method", method,
                        "uri", normalizedURI)
                .record(duration, TimeUnit.MILLISECONDS);

        String contentLength = request.getHeader("Content-Length");
        if (contentLength != null && !contentLength.isEmpty()) {
            try {
                long size = Long.parseLong(contentLength);
                meterRegistry.summary("http_request_size_bytes",
                                "method", method,
                                "uri", normalizedURI)
                        .record(size);
            } catch (NumberFormatException ignored) {
            }
        }

        Object principal = request.getUserPrincipal();
        if (principal != null) {
            String username = principal.toString();
            meterRegistry.counter("active_users_total",
                            "username", username)
                    .increment();
        }
    }

    private String normalizeURI(String uri) {
        return uri.replaceAll("/\\d+", "/{id}");
    }

    private String extractPhoneNumber(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return "Unknown";
        }

        try {
            String[] parts = token.substring(7).split("\\.");
            if (parts.length < 2) return "Invalid Token";

            String payloadJson = new String(Base64.getDecoder().decode(parts[1]));

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode payload = objectMapper.readTree(payloadJson);

            return payload.has("sub") ? payload.get("sub").asText() : "Unknown";
        } catch (Exception e) {
            logger.error("Failed to extract phone number from JWT: {}", e.getMessage());
            return "Unknown";
        }
    }
}
