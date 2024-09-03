package ktb.team6.lunchoverflow.domain.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class KakaoPlace {

    @JsonProperty("id")
    private Long kakaoId;
    @JsonProperty("place_name")
    private String placeName;
    @JsonProperty("road_address_name")
    private String roadAddressName;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("x")
    private String longitude;
    @JsonProperty("y")
    private String latitude;
    @JsonProperty("category_name")
    private String category;
    @JsonProperty("place_url")
    private String placeUrl;
    @JsonProperty("distance")
    private Long distance;
}
