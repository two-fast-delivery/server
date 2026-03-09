package nbcamp.TwoFastDelivery.global.exception;

import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.global.common.CommonResponse;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    /**
     * 서비스, 도메인에서 직접 던지는 예외
     */
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CommonResponse<Void>> handleCustomException(CustomException e) {
        ErrorCode ec = e.getErrorCode();

        String resolvedMessage = messageSource.getMessage(ec.getMessage(), null, ec.getMessage(), LocaleContextHolder.getLocale());

        return ResponseEntity
                .status(ec.getStatus())
                .body(CommonResponse.fail(resolvedMessage, ec.name()));
    }

    /**
     * Valid 검증 실패 (RequestBody DTO)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<Void>> handleValidation(MethodArgumentNotValidException e) {
        String details = e.getBindingResult().getFieldErrors().stream()
                .map(this::formatFieldError)
                .collect(Collectors.joining(", "));

        // message는 사람이 읽기 쉬운 형태
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.fail(details.isBlank() ? ErrorCode.INVALID_INPUT.getMessage() : details,
                        ErrorCode.INVALID_INPUT.name()));
    }

    /**
     * JSON 파싱 실패 / Enum 파싱 실패 등 (요청 본문이 깨졌을 때)
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CommonResponse<Void>> handleNotReadable(HttpMessageNotReadableException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(CommonResponse.fail(ErrorCode.JSON_PARSE_ERROR.getMessage(),
                        ErrorCode.JSON_PARSE_ERROR.name()));
    }

    /**
     * 마지막 안전망: 예기치 못한 모든 예외
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<Void>> handleException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonResponse.fail(ErrorCode.INTERNAL_ERROR.getMessage(),
                        ErrorCode.INTERNAL_ERROR.name()));
    }

    private String formatFieldError(FieldError fe) {
        String field = fe.getField();
        String msg = fe.getDefaultMessage();
        return field + " - " + (msg == null ? "유효하지 않습니다." : msg);
    }
}
