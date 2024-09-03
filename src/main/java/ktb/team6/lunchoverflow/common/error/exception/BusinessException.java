package ktb.team6.lunchoverflow.common.error.exception;

import ktb.team6.lunchoverflow.common.error.ErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{
    private ErrorCode errorCode;
}
