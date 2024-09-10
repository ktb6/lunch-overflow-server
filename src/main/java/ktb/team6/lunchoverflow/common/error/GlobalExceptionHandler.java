package ktb.team6.lunchoverflow.common.error;

import jakarta.servlet.http.HttpServletRequest;
import ktb.team6.lunchoverflow.common.error.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex, HttpServletRequest request){
        log.error("[Business Exception]: {}", ex.getErrorCode());
        log.error("[발생 위치]: {}, {}", request.getMethod(), request.getRequestURI());
        return ResponseEntity.status(ex.getErrorCode().getHttpStatus()).body(ErrorResponse.of(ex.getErrorCode(), request));
    }
}
