package ktb.team6.lunchoverflow.food.runner;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

                Optional<RestaurantEntity> restaurantOptional = restaurantRepository.findByName(restaurantName);

                RestaurantEntity restaurant;
                if (restaurantOptional.isEmpty()) {
                    restaurant = saveRestaurant(restaurantData);
                } else {
                    log.info("중복 레스토랑 존재, skip");
                    restaurant = restaurantOptional.get();
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
                    food.updateFoodNameAndFoodDesc(foodName, foodDesc);

                    try {
                        Long[] priceRange = parsePriceRange(priceString);
                        food.setPriceRange(priceRange[0], priceRange[1]);
                    } catch (NumberFormatException e) {
                        log.warn("Invalid price format for food item: {} in restaurant: {}. Skipping this item.",
                                foodName, restaurantName);
                        continue;
                    }

                    food.updateRestaurant(restaurant);
                    foodRepository.save(food);
                }
            }
            log.info("초기 Data 저장 완료");
        } catch (Exception e) {
            log.info("Error : " + e.getMessage());
        }
    }

    // 해당 부분은 RestaurantEntity에 맞춰서 update 예정
    private RestaurantEntity saveRestaurant(Map<String, Object> restaurantData) {
        RestaurantEntity restaurant = new RestaurantEntity();
        // restaurant 생성자 로직
//        restaurant.setAddress((String) restaurantData.get("address_name"));
//        restaurant.setName((String) restaurantData.get("name"));
//        restaurant.setDistance(parseDouble(restaurantData.get("distance")));
//        restaurant.setKakaoUrl((String) restaurantData.get("kakao_url"));
//        restaurant.setNaverUrl((String) restaurantData.get("naver_url"));
//        restaurant.setPhone((String) restaurantData.get("phone"));
//        restaurant.setLongitude(parseDouble(restaurantData.get("x")));
//        restaurant.setLatitude(parseDouble(restaurantData.get("y")));
//        restaurant.setCategory((String) restaurantData.get("category"));
//        restaurant.setReviewCount(parseDouble(restaurantData.get("review_num")));
//        restaurant.setReviewScore(parseDouble(restaurantData.get("score")));

        // Address를 도로명 주소로 할 경우
        //        restaurant.setAddress((String) restaurantData.get("road_address_name"));
        return restaurantRepository.save(restaurant);
    }

    private Long[] parsePriceRange(String priceString) {
        priceString = priceString.replaceAll("[,원\\s]", "");
        String[] prices = priceString.split("[-~/]");
        if (prices.length == 1) {
            Long price = Long.parseLong(prices[0].replaceAll("[^0-9]", ""));
            return new Long[]{price, price};
        } else if (prices.length == 2) {
            Long minPrice = Long.parseLong(prices[0].replaceAll("[^0-9]", ""));
            Long maxPrice = Long.parseLong(prices[1].replaceAll("[^0-9]", ""));
            return new Long[]{minPrice, maxPrice};
        } else {
            throw new NumberFormatException("Invalid price format");
        }
    }

    private double parseDouble(Object value) {
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else if (value instanceof String) {
            try {
                return Double.parseDouble((String) value);
            } catch (NumberFormatException e) {
                log.warn("유효하지 않은 double 값: {}", value);
                return 0.0;
            }
        }
        log.warn("예상치 못한 double 타입 값: {}", value);
        return 0.0;
    }

}
