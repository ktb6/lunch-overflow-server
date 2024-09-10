package ktb.team6.lunchoverflow.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import ktb.team6.lunchoverflow.domain.embedded.Address;
import ktb.team6.lunchoverflow.domain.embedded.Location;
import ktb.team6.lunchoverflow.domain.enums.FoodCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_id")
    private Long id;

    @Column(name = "restaurant_name", nullable = false)
    private String name;

    @Column(name = "distance", nullable = false)
    private Long distance;

    @Column(name = "phone")
    private String phone;

    @Column(name = "naver_url")
    private String naverUrl;

    @Column(name = "kakao_url")
    private String kakaoUrl;

    @Column(name = "kakao_place_id",  unique = true)
    private Long kakaoId;

    @Embedded
    private Address address;

    @Embedded
    private Location location;

    @Column(name = "raw_category")
    private String rawCategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "food_category")
    private FoodCategory foodCategory;

    @Builder
    public Restaurant(String name, Long distance, String phone, String naverUrl, String kakaoUrl, Address address,
                      Location location, String rawCategory) {
        this.name = name;
        this.distance = distance;
        this.phone = phone;
        this.naverUrl = naverUrl;
        this.kakaoUrl = kakaoUrl;
        this.address = address;
        this.location = location;
        this.rawCategory = rawCategory;
    }
}
