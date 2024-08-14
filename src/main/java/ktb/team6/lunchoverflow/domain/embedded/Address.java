package ktb.team6.lunchoverflow.domain.embedded;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    private String siDo;
    private String siGunGu;
    private String roadNameAddress;

    private Address(String siDo, String siGunGu, String roadNameAddress) {
        this.siDo = siDo;
        this.siGunGu = siGunGu;
        this.roadNameAddress = roadNameAddress;
    }

    public static Address from(String roadNameAddress) {
        return new Address(
                roadNameAddress.substring(0, 2),
                roadNameAddress.substring(2, 3),
                roadNameAddress.substring(3)
        );
    }

    public String getFullRoadNameAddress() {
        return siDo + siGunGu + roadNameAddress;
    }
}
