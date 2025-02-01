package com.alikuslu.housefit.demo.controller;

import com.alikuslu.housefit.demo.dto.GroupSessionRequest;
import com.alikuslu.housefit.demo.dto.GroupSessionResponse;
import com.alikuslu.housefit.demo.service.GroupSessionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/group-session")
public class GroupSessionController {

    private final GroupSessionService groupSessionService;

    public GroupSessionController(GroupSessionService groupSessionService) {
        this.groupSessionService = groupSessionService;
    }

    @PostMapping
    public ResponseEntity<GroupSessionResponse> createGroupSession(@RequestBody GroupSessionRequest request) {
        GroupSessionResponse response = groupSessionService.createGroupSession(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{sessionId}/join")
    public ResponseEntity<GroupSessionResponse> joinSession(
            @PathVariable Long sessionId,
            @RequestParam Long userId
    ) {
        return ResponseEntity.ok(groupSessionService.joinSession(sessionId, userId));
    }

    @GetMapping("/upcoming")
    public ResponseEntity<Page<GroupSessionResponse>> getUpcomingSessions(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(groupSessionService.getUpcomingSessions(pageable));
    }

    @GetMapping("/calendar")
    public ResponseEntity<List<GroupSessionResponse>> getCalendarSessions(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end
    ) {
        return ResponseEntity.ok(groupSessionService.getCalendarSessions(start, end));
    }
}
