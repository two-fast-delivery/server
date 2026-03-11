package nbcamp.TwoFastDelivery.store.domain;

public enum StoreStatus {
    OPEN,          // 영업 중
    CLOSED,        // 영업 종료
    PREPARING,     // 준비 중
    SHUTDOWN,      // 폐점(soft delete)
    UNDER_REVIEW   // 심사 중
}