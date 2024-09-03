package ktb.team6.lunchoverflow.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Getter
@Table(name = "food")
public class FoodEntity {

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

    public void setPriceRange(Long minPrice, Long maxPrice) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public void updateFoodNameAndFoodDesc(String foodName, String foodDesc) {
        this.name = foodName;
        this.description = foodDesc;
    }

    public void updateRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
