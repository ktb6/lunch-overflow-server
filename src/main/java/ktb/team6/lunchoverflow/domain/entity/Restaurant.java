package ktb.team6.lunchoverflow.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import ktb.team6.lunchoverflow.domain.embedded.Address;
import ktb.team6.lunchoverflow.domain.embedded.Location;
import ktb.team6.lunchoverflow.domain.service.dto.KakaoPlace;
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

    @Column(name = "restaurant_url")
    private String url;

    @Column(name = "kakao_place_id", nullable = false, unique = true)
    private Long kakaoId;

    @Embedded
    private Address address;
    //Todo 추후 Embedded 타입으로 변환 작업 필요
//    private String address;

    @Embedded
    private Location location;

    //Todo 데이터 파싱 후 추후에 수정 필요
    @Column(name = "category")
    private String category;

    private Restaurant(String name, Long distance, String phone, String url, Long kakaoId, Address address,
                       Location location, String category) {
        this.name = name;
        this.distance = distance;
        this.phone = phone;
        this.url = url;
        this.kakaoId = kakaoId;
        this.address = address;
        this.location = location;
        this.category = category;
    }

    public static Restaurant from(KakaoPlace kakaoPlace) {
        return new Restaurant(
                kakaoPlace.getPlaceName(),
                kakaoPlace.getDistance(),
                kakaoPlace.getPhone(),
                kakaoPlace.getPlaceUrl(),
                kakaoPlace.getKakaoId(),
                Address.from(kakaoPlace.getRoadAddressName()),
                Location.builder()
                        .latitude(Double.valueOf(kakaoPlace.getLatitude()))
                        .longitude(Double.valueOf(kakaoPlace.getLongitude()))
                        .build(),
                kakaoPlace.getCategory()
        );
    }
}
