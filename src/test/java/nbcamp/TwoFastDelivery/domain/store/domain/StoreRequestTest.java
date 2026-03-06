package nbcamp.TwoFastDelivery.domain.store.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.lang.reflect.Field;
import org.junit.jupiter.api.Test;

class StoreRequestTest {

    private Store getStoreField(StoreRequest request) throws Exception {
        Field field = StoreRequest.class.getDeclaredField("store");
        field.setAccessible(true);
        return (Store) field.get(request);
    }

    private String getStoreDescField(StoreRequest request) throws Exception {
        Field field = StoreRequest.class.getDeclaredField("store_desc");
        field.setAccessible(true);
        return (String) field.get(request);
    }

    private StoreRequestStatus getStatusField(StoreRequest request) throws Exception {
        Field field = StoreRequest.class.getDeclaredField("status");
        field.setAccessible(true);
        return (StoreRequestStatus) field.get(request);
    }

    @Test
    void createStoreRequest_등록요청이_PENDING_상태로_생성된다() throws Exception {
        // given
        Store store = new Store();

        // when
        StoreRequest request = StoreRequest.createStoreRequest(store, "가게 등록 요청");

        // then
        assertSame(store, getStoreField(request));
        assertEquals("가게 등록 요청", getStoreDescField(request));
        assertEquals(StoreRequestStatus.PENDING, getStatusField(request));
    }

    @Test
    void approve_요청상태가_APPROVED로_변경된다() throws Exception {
        // given
        StoreRequest request = StoreRequest.createStoreRequest(new Store(), "요청");

        // when
        request.approve();

        // then
        assertEquals(StoreRequestStatus.APPROVED, getStatusField(request));
    }

    @Test
    void reject_요청상태가_REJECTED로_변경된다() throws Exception {
        // given
        StoreRequest request = StoreRequest.createStoreRequest(new Store(), "요청");

        // when
        request.reject();

        // then
        assertEquals(StoreRequestStatus.REJECTED, getStatusField(request));
    }
}