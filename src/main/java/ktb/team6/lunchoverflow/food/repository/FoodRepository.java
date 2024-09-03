package ktb.team6.lunchoverflow.food.repository;

import ktb.team6.lunchoverflow.food.entity.FoodEntity;
import ktb.team6.lunchoverflow.food.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<FoodEntity, Long> {
    boolean existsByRestaurantAndFoodName(RestaurantEntity restaurant, String foodName);
}
