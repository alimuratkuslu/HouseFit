package com.alikuslu.housefit.demo.service;

import com.alikuslu.housefit.demo.model.CheckInStatus;
import com.alikuslu.housefit.demo.model.GroupSession;
import com.alikuslu.housefit.demo.model.SessionCheckIn;
import com.alikuslu.housefit.demo.repository.GroupSessionRepository;
import com.alikuslu.housefit.demo.repository.SessionCheckInRepository;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SessionCheckInService {
    private final GroupSessionRepository groupSessionRepository;
    private final SessionCheckInRepository checkInRepository;

    public SessionCheckInService(GroupSessionRepository groupSessionRepository, SessionCheckInRepository checkInRepository) {
        this.groupSessionRepository = groupSessionRepository;
        this.checkInRepository = checkInRepository;
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void scheduleCheckInReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = now.plusDays(1);

        List<GroupSession> upcomingSessions = groupSessionRepository
                .findByStartTimeBetweenAndCheckinsProcessedFalse(now, tomorrow);

        upcomingSessions.forEach(session -> {
            session.getParticipants().forEach(user -> {
                SessionCheckIn checkIn = new SessionCheckIn();
                checkIn.setSession(session);
                checkIn.setUser(user);
                checkIn.setStatus(CheckInStatus.PENDING);
                checkInRepository.save(checkIn);
            });
            session.setCheckinsProcessed(true);
            groupSessionRepository.save(session);
        });
    }

    @Transactional
    public void processCheckInResponse(Long checkInId, CheckInStatus status) {
        SessionCheckIn checkIn = checkInRepository.findById(checkInId)
                .orElseThrow(() -> new RuntimeException("Check-in not found"));

        if (checkIn.getStatus() != CheckInStatus.PENDING) {
            throw new RuntimeException("Check-in already processed");
        }

        if (LocalDateTime.now().isAfter(checkIn.getSession().getStartTime().minusHours(12))) {
            throw new RuntimeException("Check-in response period has expired");
        }

        checkIn.setStatus(status);
        checkIn.setRespondedAt(LocalDateTime.now());

        if (status == CheckInStatus.CANCELLED) {
            GroupSession session = checkIn.getSession();
            session.getParticipants().remove(checkIn.getUser());
            session.setCurrentParticipants(session.getCurrentParticipants() - 1);
            groupSessionRepository.save(session);
        }

        checkInRepository.save(checkIn);
    }
}
