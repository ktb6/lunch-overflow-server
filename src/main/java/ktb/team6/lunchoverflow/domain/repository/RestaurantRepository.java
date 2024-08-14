package ktb.team6.lunchoverflow.domain.repository;

import ktb.team6.lunchoverflow.domain.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}
