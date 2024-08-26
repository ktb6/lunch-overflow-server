package ktb.team6.lunchoverflow.domain.embedded;

import jakarta.persistence.Embeddable;
import java.util.Arrays;
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
        String[] parts = roadNameAddress.split(" ");
        String part1 = parts[0] + " " + parts[1];
        String part2 = parts[2];
        String part3 = String.join(" ", Arrays.copyOfRange(parts, 3, parts.length));
        return new Address(
                part1,
                part2,
                part3
        );
    }

    public String getFullRoadNameAddress() {
        return siDo + siGunGu + roadNameAddress;
    }
}
