package nbcamp.TwoFastDelivery.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 프로젝트 공통 응답 포맷
 * success: 성공 여부
 * message: 응답 메시지
 * errorCode: 실패일 때 에러 코드(성공이면 null)
 * data: 응답 데이터(없으면 null)
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse<T> {

    private final boolean success;
    private final String message;
    private final String errorCode;
    private final T data;

    public static <T> CommonResponse<T> success(String message, T data) {
        return new CommonResponse<>(true, message, null, data);
    }

    public static <T> CommonResponse<T> success(T data) {
        return new CommonResponse<>(true, "요청 성공", null, data);
    }

    public static CommonResponse<Void> success(String message) {
        return new CommonResponse<>(true, message, null, null);
    }

    public static CommonResponse<Void> success() {
        return new CommonResponse<>(true, "요청 성공", null, null);
    }

    public static <T> CommonResponse<T> fail(String message, String errorCode, T data) {
        return new CommonResponse<>(false, message, errorCode, data);
    }

    public static CommonResponse<Void> fail(String message, String errorCode) {
        return new CommonResponse<>(false, message, errorCode, null);
    }
}
