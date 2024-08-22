package ktb.team6.lunchoverflow.food.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "restaurant")
public class RestaurantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String address;
    private String name;
    private double distance;
    @Column(length = 500)
    private String kakaoUrl;
    @Column(length = 500)
    private String naverUrl;
    private String phone;
    private double longitude;
    private double latitude;
    private String category;
    private double reviewCount;
    private double reviewScore;
}
