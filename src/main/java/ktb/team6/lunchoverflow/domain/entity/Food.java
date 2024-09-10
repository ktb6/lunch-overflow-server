package ktb.team6.lunchoverflow.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "food")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_id")
    private Long id;

    @Column(name = "food_name", nullable = false)
    private String name;

    @Column(name = "food_description")
    private String description;
    private Long minPrice;
    private Long maxPrice;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Builder
    public Food(String name, String description, Long minPrice, Long maxPrice, Restaurant restaurant) {
        this.name = name;
        this.description = description;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.restaurant = restaurant;
    }

    public void updatePrices(Long minPrice, Long maxPrice) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public void updateRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
