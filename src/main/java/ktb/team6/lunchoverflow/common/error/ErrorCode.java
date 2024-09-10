package ktb.team6.lunchoverflow.common.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    ;

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;
}
