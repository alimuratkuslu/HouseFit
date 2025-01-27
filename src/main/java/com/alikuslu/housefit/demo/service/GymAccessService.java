package com.alikuslu.housefit.demo.service;

import com.alikuslu.housefit.demo.model.GymAccess;
import com.alikuslu.housefit.demo.model.User;
import com.alikuslu.housefit.demo.repository.GymAccessRepository;
import com.alikuslu.housefit.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GymAccessService {

    private final GymAccessRepository gymAccessRepository;
    private final UserRepository userRepository;

    public GymAccessService(GymAccessRepository gymAccessRepository, UserRepository userRepository) {
        this.gymAccessRepository = gymAccessRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public GymAccess recordAccess(String qrCode, User currentUser) throws BadRequestException {
        String[] parts = qrCode.split("_");
        if (parts.length != 4 || !parts[0].equals("HOUSEFIT") || !parts[1].equals("GYM")) {
            throw new BadRequestException("Invalid QR code format");
        }

        GymAccess.AccessType accessType;
        try {
            accessType = GymAccess.AccessType.valueOf(parts[2]);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid access type");
        }

        Long locationId;
        try {
            locationId = Long.parseLong(parts[3]);
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid location ID");
        }

        Optional<GymAccess> lastAccess = gymAccessRepository.findFirstByUserOrderByTimestampDesc(currentUser);
        if (lastAccess.isPresent() && lastAccess.get().getAccessType() == accessType) {
            throw new BadRequestException("Cannot record " + accessType.toString().toLowerCase() +
                    " twice in a row. Please scan the " +
                    (accessType == GymAccess.AccessType.ENTRY ? "exit" : "entry") + " QR code.");
        }

        GymAccess gymAccess = new GymAccess();
        gymAccess.setUser(currentUser);
        gymAccess.setAccessType(accessType);
        gymAccess.setTimestamp(LocalDateTime.now());
        gymAccess.setLocationId(locationId);

        return gymAccessRepository.save(gymAccess);
    }

    public List<GymAccess> getUserAccessHistory(User user) {
        return gymAccessRepository.findByUserOrderByTimestampDesc(user);
    }

    public List<GymAccess> getUserAccessHistoryBetweenDates(
            User user,
            LocalDateTime startTime,
            LocalDateTime endTime
    ) {
        return gymAccessRepository.findByUserAndTimestampBetweenOrderByTimestampDesc(
                user,
                startTime,
                endTime
        );
    }
}
