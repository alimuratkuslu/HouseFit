package com.alikuslu.housefit.demo.service;

import com.alikuslu.housefit.demo.dto.GroupSessionRequest;
import com.alikuslu.housefit.demo.dto.GroupSessionResponse;
import com.alikuslu.housefit.demo.model.GroupSession;
import com.alikuslu.housefit.demo.model.SessionStatus;
import com.alikuslu.housefit.demo.model.User;
import com.alikuslu.housefit.demo.repository.GroupSessionRepository;
import com.alikuslu.housefit.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class GroupSessionService {

    private final GroupSessionRepository groupSessionRepository;
    private final UserRepository userRepository;

    public GroupSessionService(GroupSessionRepository groupSessionRepository, UserRepository userRepository) {
        this.groupSessionRepository = groupSessionRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public GroupSessionResponse createGroupSession(GroupSessionRequest request) {
        User trainer = userRepository.findById(request.getTrainerId())
                .orElseThrow(() -> new RuntimeException("Trainer not found"));

        GroupSession session = new GroupSession();
        session.setName(request.getName());
        session.setDescription(request.getDescription());
        session.setPhotoUrl(request.getPhotoUrl());
        session.setTrainer(trainer);
        session.setParticipantLimit(request.getParticipantLimit());
        session.setStartTime(request.getStartTime());
        session.setEndTime(request.getEndTime());

        GroupSession savedSession = groupSessionRepository.save(session);
        return GroupSessionResponse.fromEntity(savedSession);
    }

    @Transactional
    public GroupSessionResponse joinSession(Long sessionId, Long userId) {
        GroupSession session = groupSessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (session.getCurrentParticipants() >= session.getParticipantLimit()) {
            throw new RuntimeException("Session is full");
        }

        if (session.getParticipants().contains(user)) {
            throw new RuntimeException("User already joined");
        }

        session.getParticipants().add(user);
        session.setCurrentParticipants(session.getCurrentParticipants() + 1);

        GroupSession updatedSession = groupSessionRepository.save(session);
        return GroupSessionResponse.fromEntity(updatedSession);
    }

    @Transactional
    public Page<GroupSessionResponse> getUpcomingSessions(Pageable pageable) {
        return groupSessionRepository.findByStatus(SessionStatus.UPCOMING, pageable)
                .map(GroupSessionResponse::fromEntity);
    }

    @Transactional
    public List<GroupSessionResponse> getCalendarSessions(LocalDate start, LocalDate end) {
        LocalDateTime startDateTime = start.atStartOfDay();
        LocalDateTime endDateTime = end.atTime(23, 59, 59);

        return groupSessionRepository.findBetweenDates(startDateTime, endDateTime).stream()
                .map(GroupSessionResponse::fromEntity)
                .toList();
    }
}
