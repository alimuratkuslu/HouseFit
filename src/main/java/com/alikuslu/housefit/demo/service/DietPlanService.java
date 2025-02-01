package com.alikuslu.housefit.demo.service;

import com.alikuslu.housefit.demo.dto.DietPlanRequest;
import com.alikuslu.housefit.demo.dto.DietPlanResponse;
import com.alikuslu.housefit.demo.model.DietPlan;
import com.alikuslu.housefit.demo.model.Meal;
import com.alikuslu.housefit.demo.model.MealFood;
import com.alikuslu.housefit.demo.model.User;
import com.alikuslu.housefit.demo.repository.DietPlanRepository;
import com.alikuslu.housefit.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DietPlanService {

    private final DietPlanRepository dietPlanRepository;
    private final UserRepository userRepository;

    public DietPlanService(DietPlanRepository dietPlanRepository, UserRepository userRepository) {
        this.dietPlanRepository = dietPlanRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public DietPlanResponse createDietPlan(DietPlanRequest request) {
        User customer = userRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        User trainer = userRepository.findById(request.getTrainerId())
                .orElseThrow(() -> new RuntimeException("Trainer not found"));

        DietPlan plan = new DietPlan();
        plan.setCustomerId(request.getCustomerId());
        plan.setTrainerId(request.getTrainerId());
        plan.setStartDate(request.getStartDate());
        plan.setEndDate(request.getEndDate());

        List<Meal> meals = request.getMeals().stream()
                .map(mealRequest -> {
                    Meal meal = new Meal();
                    meal.setType(mealRequest.getType());
                    meal.setTime(mealRequest.getTime());
                    meal.setItems(mealRequest.getItems().stream()
                            .map(foodRequest -> {
                                MealFood food = new MealFood();
                                food.setFoodId(foodRequest.getFoodId());
                                food.setLabel(foodRequest.getLabel());
                                food.setPortion(foodRequest.getPortion());
                                food.setCalories(foodRequest.getCalories());
                                food.setProtein(foodRequest.getProtein());
                                food.setCarbs(foodRequest.getCarbs());
                                food.setFats(foodRequest.getFats());
                                return food;
                            }).toList());
                    meal.setDietPlan(plan);
                    return meal;
                }).toList();

        plan.setMeals(meals);
        DietPlan savedPlan = dietPlanRepository.save(plan);
        return DietPlanResponse.fromEntity(savedPlan);
    }

    @Transactional
    public DietPlanResponse getDietPlan(Long planId) {
        DietPlan plan = dietPlanRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Diet plan not found"));

        Hibernate.initialize(plan.getMeals());
        plan.getMeals().forEach(meal -> Hibernate.initialize(meal.getItems()));

        return DietPlanResponse.fromEntity(plan);
    }

    public List<DietPlanResponse> getCustomerDietPlans(Long customerId) {
        List<DietPlan> plans = dietPlanRepository.findByCustomerId(customerId);
        return plans.stream()
                .map(DietPlanResponse::fromEntity)
                .toList();
    }
}
