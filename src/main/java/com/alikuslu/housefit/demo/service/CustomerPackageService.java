package com.alikuslu.housefit.demo.service;

import com.alikuslu.housefit.demo.dto.AssignPackageRequest;
import com.alikuslu.housefit.demo.dto.CustomerPackageDto;
import com.alikuslu.housefit.demo.model.*;
import com.alikuslu.housefit.demo.repository.CustomerPackageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerPackageService {

    private final CustomerPackageRepository customerPackageRepository;
    private final UserService userService;
    private final FitnessPackageService fitnessPackageService;

    public CustomerPackageService(CustomerPackageRepository customerPackageRepository,
                                  UserService userService,
                                  FitnessPackageService fitnessPackageService) {
        this.customerPackageRepository = customerPackageRepository;
        this.userService = userService;
        this.fitnessPackageService = fitnessPackageService;
    }

    public List<CustomerPackageDto> getCustomerPackages(Long customerId) {
        User customer = userService.findById(customerId);
        return customerPackageRepository.findByCustomer(customer).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<CustomerPackageDto> getActiveCustomerPackages(Long customerId) {
        User customer = userService.findById(customerId);
        return customerPackageRepository.findByCustomerAndStatus(customer, PackageStatus.ACTIVE).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<CustomerPackageDto> getTrainerAssignedPackages(Long trainerId) {
        User trainer = userService.findById(trainerId);
        return customerPackageRepository.findByAssignedBy(trainer).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public CustomerPackageDto assignPackage(AssignPackageRequest request, String trainerUsername) {
        User trainer = userService.findByUsername(trainerUsername);

        if (trainer.getUserType() != UserType.TRAINER && trainer.getUserType() != UserType.ADMIN) {
            throw new RuntimeException("Only trainers and admins can assign packages");
        }

        User customer = userService.findById(request.getCustomerId());

        if (customer.getUserType() != UserType.CUSTOMER) {
            throw new RuntimeException("Packages can only be assigned to customers");
        }

        FitnessPackage fitnessPackage = fitnessPackageService.getPackageEntityById(request.getPackageId());

        LocalDate startDate = request.getStartDate() != null ? request.getStartDate() : LocalDate.now();
        LocalDate endDate = startDate.plusDays(fitnessPackage.getDuration());

        CustomerPackage customerPackage = CustomerPackage.builder()
                .customer(customer)
                .fitnessPackage(fitnessPackage)
                .assignedBy(trainer)
                .startDate(startDate)
                .endDate(endDate)
                .remainingSessions(fitnessPackage.getTotalSessions())
                .status(PackageStatus.ACTIVE)
                .notes(request.getNotes())
                .build();

        customerPackage = customerPackageRepository.save(customerPackage);
        return mapToDto(customerPackage);
    }

    public CustomerPackageDto completeSession(Long packageId, String trainerUsername) {
        User trainer = userService.findByUsername(trainerUsername);

        if (trainer.getUserType() != UserType.TRAINER && trainer.getUserType() != UserType.ADMIN) {
            throw new RuntimeException("Only trainers and admins can mark sessions as completed");
        }

        CustomerPackage customerPackage = customerPackageRepository.findById(packageId)
                .orElseThrow(() -> new RuntimeException("Customer package not found"));

        if (!customerPackage.getAssignedBy().getId().equals(trainer.getId()) && trainer.getUserType() != UserType.ADMIN) {
            throw new RuntimeException("You can only manage packages you assigned");
        }

        if (customerPackage.getStatus() != PackageStatus.ACTIVE) {
            throw new RuntimeException("Cannot complete session for a non-active package");
        }

        if (customerPackage.getRemainingSessions() <= 0) {
            throw new RuntimeException("No remaining sessions in this package");
        }

        customerPackage.setRemainingSessions(customerPackage.getRemainingSessions() - 1);

        if (customerPackage.getRemainingSessions() <= 0 || LocalDate.now().isAfter(customerPackage.getEndDate())) {
            customerPackage.setStatus(PackageStatus.COMPLETED);
        }

        customerPackage = customerPackageRepository.save(customerPackage);
        return mapToDto(customerPackage);
    }

    public CustomerPackageDto getCustomerPackageById(Long id) {
        CustomerPackage customerPackage = customerPackageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer package not found"));
        return mapToDto(customerPackage);
    }

    public void cancelPackage(Long id, String username) {
        User user = userService.findByUsername(username);
        CustomerPackage customerPackage = customerPackageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer package not found"));

        if (!customerPackage.getAssignedBy().getId().equals(user.getId()) &&
                !customerPackage.getCustomer().getId().equals(user.getId()) &&
                user.getUserType() != UserType.ADMIN) {
            throw new RuntimeException("You don't have permission to cancel this package");
        }

        customerPackage.setStatus(PackageStatus.CANCELLED);
        customerPackageRepository.save(customerPackage);
    }

    private CustomerPackageDto mapToDto(CustomerPackage customerPackage) {
        return CustomerPackageDto.builder()
                .id(customerPackage.getId())
                .customerId(customerPackage.getCustomer().getId())
                .customerName(customerPackage.getCustomer().getName() + " " + customerPackage.getCustomer().getSurname())
                .packageId(customerPackage.getFitnessPackage().getId())
                .packageName(customerPackage.getFitnessPackage().getName())
                .assignedById(customerPackage.getAssignedBy().getId())
                .assignedByName(customerPackage.getAssignedBy().getName() + " " + customerPackage.getAssignedBy().getSurname())
                .startDate(customerPackage.getStartDate())
                .endDate(customerPackage.getEndDate())
                .remainingSessions(customerPackage.getRemainingSessions())
                .status(customerPackage.getStatus())
                .notes(customerPackage.getNotes())
                .build();
    }
}
