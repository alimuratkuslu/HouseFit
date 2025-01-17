package com.alikuslu.housefit.demo.service;

import com.alikuslu.housefit.demo.dto.DietPlanDto;
import com.alikuslu.housefit.demo.dto.MealDto;
import com.alikuslu.housefit.demo.model.DietPlan;
import com.alikuslu.housefit.demo.model.Meal;
import com.alikuslu.housefit.demo.model.MealFood;
import com.alikuslu.housefit.demo.model.User;
import com.alikuslu.housefit.demo.repository.DietPlanRepository;
import com.alikuslu.housefit.demo.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DietPlanService {

    private final DietPlanRepository dietPlanRepository;
    private final UserRepository userRepository;

    public DietPlanService(DietPlanRepository dietPlanRepository, UserRepository userRepository) {
        this.dietPlanRepository = dietPlanRepository;
        this.userRepository = userRepository;
    }

    public DietPlan createDietPlan(DietPlanDto dto) {
        User user = userRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        DietPlan dietPlan = new DietPlan();
        dietPlan.setCustomerId(user.getId());
        dietPlan.setTrainerId(dto.getTrainerId());
        dietPlan.setStartDate(dto.getStartDate());
        dietPlan.setEndDate(dto.getEndDate());
        dietPlan.setMeals(dto.getMeals());

        return dietPlanRepository.save(dietPlan);
    }

    public DietPlan getDietPlanByUserId(Long userId) {
        return dietPlanRepository.findByCustomerId(userId)
                .orElseThrow(() -> new RuntimeException("Diet Plan not found"));
    }
}
