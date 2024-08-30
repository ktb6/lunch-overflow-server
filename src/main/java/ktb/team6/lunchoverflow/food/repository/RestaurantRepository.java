package ktb.team6.lunchoverflow.food.repository;


import java.util.Optional;
import ktb.team6.lunchoverflow.food.entity.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<RestaurantEntity, Long> {
    Optional<RestaurantEntity> findByName(String name);
}
