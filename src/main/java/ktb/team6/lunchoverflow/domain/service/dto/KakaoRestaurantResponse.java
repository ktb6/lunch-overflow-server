package ktb.team6.lunchoverflow.domain.service.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
public class KakaoRestaurantResponse {

    @Setter
    private List<KakaoPlace> documents;
    private Meta meta;
}

class Meta {

}