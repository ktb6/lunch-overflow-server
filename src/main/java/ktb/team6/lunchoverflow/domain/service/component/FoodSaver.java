package ktb.team6.lunchoverflow.domain.service.component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ktb.team6.lunchoverflow.domain.embedded.Address;
import ktb.team6.lunchoverflow.domain.embedded.Location;
import ktb.team6.lunchoverflow.domain.entity.Food;
import ktb.team6.lunchoverflow.domain.entity.Restaurant;
import ktb.team6.lunchoverflow.domain.repository.RestaurantRepository;
import ktb.team6.lunchoverflow.domain.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class FoodSaver {

    private final RestaurantRepository restaurantRepository;
    private final FoodRepository foodRepository;

    @Transactional
    public void saveInitData() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File("restaurant_list_final.json");
        List<Map<String, Object>> jsonData = objectMapper.readValue(
                file,
                new TypeReference<>() {
                }
        );

        for (Map<String, Object> restaurantData : jsonData) {
            Restaurant restaurant = saveRestaurant(restaurantData);
            List<Map<String, Object>> menu = (List<Map<String, Object>>) restaurantData.get("menu");
            if (menu != null) {
                saveMenus(menu, restaurant);
            }
        }
        log.info("초기 Data 저장 완료");
    }


    private void saveMenus(List<Map<String, Object>> menuData, Restaurant restaurant) {
        List<Food> foods = new ArrayList<>();
        for (Map<String, Object> menuItem : menuData) {
            Food food = Food.builder()
                    .name((String) menuItem.get("name"))
                    .description((String) menuItem.get("description"))
                    .build();
            String priceString = (String) menuItem.get("price");

            try {
                Long[] priceRange = parsePriceRange(priceString);
                food.updatePrices(priceRange[0], priceRange[1]);
            } catch (NumberFormatException e) {
                log.warn("Invalid price format for food item: {} in restaurant: {}. Skipping this item.",
                        food.getName(), restaurant.getName());
                continue;
            }

            food.updateRestaurant(restaurant);
            foods.add(food);
        }
        foodRepository.saveAll(foods);
    }

    private Restaurant saveRestaurant(Map<String, Object> restaurantData) {
        return restaurantRepository.save(Restaurant.builder()
                .name((String) restaurantData.get("name"))
                .distance(Math.round((Double) restaurantData.get("distance") * 10000))
                .rawCategory((String) restaurantData.get("category"))
                .phone((String) restaurantData.get("phone"))
                .location(Location.builder()
                        .latitude(Double.parseDouble((String) restaurantData.get("y")))
                        .longitude(Double.parseDouble((String) restaurantData.get("x")))
                        .build())
                .address(Address.from((String) restaurantData.get("road_address_name")))
                .build());
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
}
