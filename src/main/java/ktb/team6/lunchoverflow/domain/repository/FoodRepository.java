package ktb.team6.lunchoverflow.domain.repository;

import ktb.team6.lunchoverflow.domain.entity.FoodEntity;
import ktb.team6.lunchoverflow.domain.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<FoodEntity, Long> {
    boolean existsByRestaurantAndName(Restaurant restaurant, String name);
}
