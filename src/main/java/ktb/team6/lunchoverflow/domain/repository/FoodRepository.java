package ktb.team6.lunchoverflow.domain.repository;

import ktb.team6.lunchoverflow.domain.entity.Food;
import ktb.team6.lunchoverflow.domain.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food, Long> {
    boolean existsByRestaurantAndName(Restaurant restaurant, String name);
}
