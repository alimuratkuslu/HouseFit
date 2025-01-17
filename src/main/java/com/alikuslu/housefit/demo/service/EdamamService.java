package com.alikuslu.housefit.demo.service;

import com.alikuslu.housefit.demo.dto.EdamamResponse;
import com.alikuslu.housefit.demo.dto.Food;
import com.alikuslu.housefit.demo.dto.FoodDto;
import com.alikuslu.housefit.demo.dto.Nutrients;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EdamamService {
    private final String APP_ID = "e5506f9f";
    private final String APP_KEY = "0f1e18e1c81f1d412dc34cd6f6e90f87";
    private final String BASE_URL = "https://api.edamam.com/api/food-database/v2";
    private final RestTemplate restTemplate;

    public EdamamService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<FoodDto> searchFoods(String query) {
        try {
            String url = String.format("%s/parser?app_id=%s&app_key=%s&ingr=%s",
                    BASE_URL, APP_ID, APP_KEY, query);

            ResponseEntity<EdamamResponse> response =
                    restTemplate.getForEntity(url, EdamamResponse.class);

            if (response.getBody() != null && response.getBody().getParsed() != null) {
                return response.getBody().getParsed().stream()
                        .map(parsedFood -> {
                            Food food = parsedFood.getFood();
                            Nutrients nutrients = food.getNutrients();
                            return FoodDto.builder()
                                    .foodId(food.getFoodId())
                                    .label(food.getLabel())
                                    .calories(nutrients.getCalories())
                                    .protein(nutrients.getProtein())
                                    .carbs(nutrients.getCarbs())
                                    .fats(nutrients.getFat())
                                    .fiber(nutrients.getFiber())
                                    .build();
                        })
                        .collect(Collectors.toList());
            }
            return new ArrayList<>();
        } catch (Exception e) {
            log.error("Error searching foods: ", e);
            return new ArrayList<>();
        }
    }
}
