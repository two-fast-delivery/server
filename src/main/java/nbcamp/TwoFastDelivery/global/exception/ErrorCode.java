package nbcamp.TwoFastDelivery.global.exception;

import lombok.Getter;
import nbcamp.TwoFastDelivery.domain.review.entity.Review;
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


    // 리뷰
    REVIEW_ALREADY_EXISTS(HttpStatus.CONFLICT, "주문 ID에 해당하는 리뷰가 이미 존재합니다."),
    REVIEW_NOT_EXISTS(HttpStatus.NOT_FOUND, "주문 ID에 해당하는 리뷰가 존재하지 않습니다."),
    REVIEW_NOT_ACTIVE(HttpStatus.NOT_FOUND, "리뷰가 활성화되어 있지 않습니다."),
    REVIEW_NOT_EQUAL_USER(HttpStatus.FORBIDDEN, "리뷰 작성자만이 삭제가 가능합니다."),

    //리뷰 신고
    REPORT_NOT_EXISTS(HttpStatus.NOT_FOUND, "신고 ID에 해당하는 리뷰가 존재하지 않습니다."),

    // 배송지
    ADDRESS_ALIAS_REQUIRED(HttpStatus.BAD_REQUEST, "address.alias.required"),
    ADDRESS_ALIAS_TOO_LONG(HttpStatus.BAD_REQUEST, "address.alias.too_long"),
    ADDRESS_VALUE_REQUIRED(HttpStatus.BAD_REQUEST, "address.address.required"),
    ADDRESS_VALUE_TOO_LONG(HttpStatus.BAD_REQUEST, "address.address.too_long"),
    DETAIL_ADDRESS_REQUIRED(HttpStatus.BAD_REQUEST, "address.detail.required"),
    DETAIL_ADDRESS_TOO_LONG(HttpStatus.BAD_REQUEST, "address.detail.too_long"),
    ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "address.not_found"),

    // 상품
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),
    PRODUCT_GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "상품 그룹을 찾을 수 없습니다."),
    PRODUCT_OPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "상품 옵션을 찾을 수 없습니다."),
    PRODUCT_OPTION_GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "상품 옵션 그룹을 찾을 수 없습니다."),
    INVALID_PRODUCT_DATA(HttpStatus.BAD_REQUEST, "상품 데이터가 올바르지 않습니다.");

    private final HttpStatus status;
    private final String message;


    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
