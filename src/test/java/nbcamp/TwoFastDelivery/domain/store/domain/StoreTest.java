package nbcamp.TwoFastDelivery.domain.store.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import org.junit.jupiter.api.Test;

class StoreTest {

    private String getStringField(Store store, String fieldName) throws Exception {
        Field field = Store.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return (String) field.get(store);
    }

    private StoreStatus getStatusField(Store store) throws Exception {
        Field field = Store.class.getDeclaredField("status");
        field.setAccessible(true);
        return (StoreStatus) field.get(store);
    }

    @Test
    void changeBasicInfo_가게_기본정보가_변경된다() throws Exception {
        // given
        Store store = new Store();

        // when
        store.changeBasicInfo(
                "새 가게",
                "서울시 어딘가",
                "010-1234-5678",
                "https://image.test",
                "09:00",
                "21:00",
                "맛있는 집"
        );

        // then
        assertEquals("새 가게", getStringField(store, "name"));
        assertEquals("서울시 어딘가", getStringField(store, "address"));
        assertEquals("010-1234-5678", getStringField(store, "phone"));
        assertEquals("https://image.test", getStringField(store, "thumbnail_url"));
        assertEquals("09:00", getStringField(store, "open_time"));
        assertEquals("21:00", getStringField(store, "close_time"));
        assertEquals("맛있는 집", getStringField(store, "description"));
    }

    @Test
    void changeStatus_가게_상태가_변경된다() throws Exception {
        // given
        Store store = new Store();
        store.changeStatus(StoreStatus.PREPARING);

        // when
        store.changeStatus(StoreStatus.OPEN);

        // then
        assertEquals(StoreStatus.OPEN, getStatusField(store));
    }
}