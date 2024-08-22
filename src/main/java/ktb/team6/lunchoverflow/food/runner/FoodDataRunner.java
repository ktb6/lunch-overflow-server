package ktb.team6.lunchoverflow.food.runner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.List;
import java.util.Map;
import ktb.team6.lunchoverflow.food.entity.FoodEntity;
import ktb.team6.lunchoverflow.food.entity.RestaurantEntity;
import ktb.team6.lunchoverflow.food.repository.FoodRepository;
import ktb.team6.lunchoverflow.food.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class FoodDataRunner implements CommandLineRunner {

    private RestaurantRepository restaurantRepository;
    private FoodRepository foodRepository;

    @Override
    public void run(String... args) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            File file = new File("restaurant_list.json");
            List<Map<String, Object>> jsonData = objectMapper.readValue(
                    file,
                    new TypeReference<List<Map<String, Object>>>() {
                    }
            );

            for (Map<String, Object> restaurantData : jsonData) {
                String restaurantName = (String) restaurantData.get("name");
                List<Map<String, Object>> menu = (List<Map<String, Object>>) restaurantData.get("menu");

                if (menu == null || menu.isEmpty()) {
                    log.info("Menu 없음 : {}", restaurantName);
                    continue;
                }

                RestaurantEntity restaurant = restaurantRepository.findByName(restaurantName);
                if (restaurant == null) {
                    restaurant = new RestaurantEntity();
                    restaurant.setName(restaurantName);
                    restaurant = restaurantRepository.save(restaurant);
                } else {
                    log.info("중복 레스토랑 존재, skip");
                }

                for (Map<String, Object> menuItem : menu) {
                    String foodName = (String) menuItem.get("name");
                    String foodDesc = (String) menuItem.get("description");
                    String priceString = (String) menuItem.get("price");

                    if (foodName == null || priceString == null) {
                        log.warn("Food name or price is missing for a menu item in restaurant: {}. Skipping this item.",
                                restaurantName);
                        continue;
                    }

                    // 음식 중복 체크
                    if (foodRepository.existsByRestaurantAndFoodName(restaurant, foodName)) {
                        log.info("Skipping duplicate food item: {} in restaurant: {}", foodName, restaurantName);
                        continue;
                    }

                    FoodEntity food = new FoodEntity();
                    food.setFoodName(foodName);
                    food.setFoodDesc(foodDesc);

                    try {
                        Long[] priceRange = parsePriceRange(priceString);
                        food.setPriceRange(priceRange[0], priceRange[1]);
                    } catch (NumberFormatException e) {
                        log.warn("Invalid price format for food item: {} in restaurant: {}. Skipping this item.",
                                foodName, restaurantName);
                        continue;
                    }

                    food.setRestaurant(restaurant);
                    foodRepository.save(food);
                }
            }
            log.info("초기 Data 저장 완료");
        } catch (Exception e) {
            log.info("Error : " + e.getMessage());
        }
    }

    private Long[] parsePriceRange(String priceString) {
        priceString = priceString.replaceAll("[,원\\s]", "");
        String[] prices = priceString.split("[-~/]");
        if (prices.length == 1) {
            // 단일 가격
            Long price = Long.parseLong(prices[0].replaceAll("[^0-9]", ""));
            return new Long[]{price, price};
        } else if (prices.length == 2) {
            // 가격 범위
            Long minPrice = Long.parseLong(prices[0].replaceAll("[^0-9]", ""));
            Long maxPrice = Long.parseLong(prices[1].replaceAll("[^0-9]", ""));
            return new Long[]{minPrice, maxPrice};
        } else {
            throw new NumberFormatException("Invalid price format");
        }
    }
}
