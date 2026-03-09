package nbcamp.TwoFastDelivery.global.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PageResponse<T> {

    private final int page;            // 현재 페이지(0-base)
    private final int size;            // 요청/적용된 페이지 크기
    private final long totalElements;  // 전체 요소 수 (Slice면 -1)
    private final int totalPages;      // 전체 페이지 수 (Slice면 -1)
    private final boolean hasNext;     // 다음 페이지 존재 여부
    private final List<T> contents;    // 실제 데이터 목록

    private static final Set<Integer> ALLOWED_SIZES = Set.of(10, 30, 50);
    private static final int DEFAULT_SIZE = 10;

    /**
     * 발제문 규칙: size는 10/30/50만 허용, 그 외는 10으로 고정
     */
    public static int normalizeSize(Integer size) {
        if (size == null) return DEFAULT_SIZE;
        return ALLOWED_SIZES.contains(size) ? size : DEFAULT_SIZE;
    }

    // ===== Page 변환 =====

    public static <T> PageResponse<T> from(Page<T> pageResult) {
        return new PageResponse<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.hasNext(),
                pageResult.getContent()
        );
    }

    public static <T, R> PageResponse<R> from(Page<T> pageResult, Function<T, R> mapper) {
        List<R> mapped = pageResult.getContent().stream()
                .map(mapper)
                .collect(Collectors.toList());

        return new PageResponse<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.hasNext(),
                mapped
        );
    }

    // ===== Slice 변환 (총 개수/총 페이지 없음) =====

    public static <T> PageResponse<T> from(Slice<T> sliceResult) {
        return new PageResponse<>(
                sliceResult.getNumber(),
                sliceResult.getSize(),
                -1L,
                -1,
                sliceResult.hasNext(),
                sliceResult.getContent()
        );
    }

    public static <T, R> PageResponse<R> from(Slice<T> sliceResult, Function<T, R> mapper) {
        List<R> mapped = sliceResult.getContent().stream()
                .map(mapper)
                .collect(Collectors.toList());

        return new PageResponse<>(
                sliceResult.getNumber(),
                sliceResult.getSize(),
                -1L,
                -1,
                sliceResult.hasNext(),
                mapped
        );
    }
}