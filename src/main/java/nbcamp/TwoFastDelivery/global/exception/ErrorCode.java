package nbcamp.TwoFastDelivery.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 공통
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "입력 값이 유효하지 않습니다."),
    JSON_PARSE_ERROR(HttpStatus.BAD_REQUEST, "요청 형식(JSON)을 확인해주세요."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    CONFLICT(HttpStatus.CONFLICT, "요청이 충돌했습니다."),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),

    // 배송지
    ADDRESS_ALIAS_REQUIRED(HttpStatus.BAD_REQUEST, "address.alias.required"),
    ADDRESS_ALIAS_TOO_LONG(HttpStatus.BAD_REQUEST, "address.alias.too_long"),
    ADDRESS_VALUE_REQUIRED(HttpStatus.BAD_REQUEST, "address.address.required"),
    ADDRESS_VALUE_TOO_LONG(HttpStatus.BAD_REQUEST, "address.address.too_long"),
    DETAIL_ADDRESS_REQUIRED(HttpStatus.BAD_REQUEST, "address.detail.required"),
    DETAIL_ADDRESS_TOO_LONG(HttpStatus.BAD_REQUEST, "address.detail.too_long"),
    ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "address.not_found"),

    ;

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
