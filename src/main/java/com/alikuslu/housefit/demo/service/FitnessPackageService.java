package com.alikuslu.housefit.demo.service;

import com.alikuslu.housefit.demo.dto.CreatePackageRequest;
import com.alikuslu.housefit.demo.dto.FitnessPackageDto;
import com.alikuslu.housefit.demo.model.FitnessPackage;
import com.alikuslu.housefit.demo.model.User;
import com.alikuslu.housefit.demo.model.UserType;
import com.alikuslu.housefit.demo.repository.FitnessPackageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FitnessPackageService {

    private final FitnessPackageRepository fitnessPackageRepository;
    private final UserService userService;

    public FitnessPackageService(FitnessPackageRepository fitnessPackageRepository, UserService userService) {
        this.fitnessPackageRepository = fitnessPackageRepository;
        this.userService = userService;
    }

    public List<FitnessPackageDto> getAllActivePackages() {
        return fitnessPackageRepository.findByActiveTrue().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<FitnessPackageDto> getPackagesByTrainer(Long trainerId) {
        return fitnessPackageRepository.findByCreatedById(trainerId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public FitnessPackageDto getPackageById(Long id) {
        FitnessPackage fitnessPackage = fitnessPackageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Package not found"));
        return mapToDto(fitnessPackage);
    }

    public FitnessPackageDto createPackage(CreatePackageRequest request, String username) {
        User trainer = userService.findByUsername(username);

        if (trainer.getUserType() != UserType.TRAINER && trainer.getUserType() != UserType.ADMIN) {
            throw new RuntimeException("Only trainers and admins can create packages");
        }

        FitnessPackage fitnessPackage = FitnessPackage.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .duration(request.getDuration())
                .totalSessions(request.getTotalSessions())
                .features(request.getFeatures())
                .active(true)
                .createdBy(trainer)
                .build();

        fitnessPackage = fitnessPackageRepository.save(fitnessPackage);
        return mapToDto(fitnessPackage);
    }

    public FitnessPackageDto updatePackage(Long id, CreatePackageRequest request, String username) {
        User trainer = userService.findByUsername(username);
        FitnessPackage fitnessPackage = fitnessPackageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Package not found"));

        if (!fitnessPackage.getCreatedBy().getId().equals(trainer.getId()) && trainer.getUserType() != UserType.ADMIN) {
            throw new RuntimeException("You can only update packages you created");
        }

        fitnessPackage.setName(request.getName());
        fitnessPackage.setDescription(request.getDescription());
        fitnessPackage.setPrice(request.getPrice());
        fitnessPackage.setDuration(request.getDuration());
        fitnessPackage.setTotalSessions(request.getTotalSessions());
        fitnessPackage.setFeatures(request.getFeatures());

        fitnessPackage = fitnessPackageRepository.save(fitnessPackage);
        return mapToDto(fitnessPackage);
    }

    public void deactivatePackage(Long id, String username) {
        User trainer = userService.findByUsername(username);
        FitnessPackage fitnessPackage = fitnessPackageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Package not found"));

        if (!fitnessPackage.getCreatedBy().getId().equals(trainer.getId()) && trainer.getUserType() != UserType.ADMIN) {
            throw new RuntimeException("You can only deactivate packages you created");
        }

        fitnessPackage.setActive(false);
        fitnessPackageRepository.save(fitnessPackage);
    }

    public FitnessPackage getPackageEntityById(Long id) {
        return fitnessPackageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Package not found"));
    }

    private FitnessPackageDto mapToDto(FitnessPackage fitnessPackage) {
        return FitnessPackageDto.builder()
                .id(fitnessPackage.getId())
                .name(fitnessPackage.getName())
                .description(fitnessPackage.getDescription())
                .price(fitnessPackage.getPrice())
                .duration(fitnessPackage.getDuration())
                .totalSessions(fitnessPackage.getTotalSessions())
                .features(fitnessPackage.getFeatures())
                .active(fitnessPackage.getActive())
                .createdById(fitnessPackage.getCreatedBy().getId())
                .createdByName(fitnessPackage.getCreatedBy().getName() + " " + fitnessPackage.getCreatedBy().getSurname())
                .build();
    }
}
