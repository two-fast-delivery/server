package nbcamp.TwoFastDelivery.store.domain;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "p_store_request")
public class StoreRequest {
    @Id
    @Column(name = "id", nullable = false, columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(name = "store_desc", nullable = false)
    private String store_desc;

    @Column(name = "status", nullable = false)
    private StoreRequestStatus status;

    StoreRequestType requestType;
    
    @PrePersist
    void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }
    
    //등록 요청용
    public static StoreRequest createStoreRequest(Store store, String desc) {
        StoreRequest req = new StoreRequest();
        req.store = store;
        req.requestType = StoreRequestType.REGISTRATION;
        req.store_desc = desc != null ? desc : "";
        req.status = StoreRequestStatus.PENDING;
        return req;
    }

    public void approve() {
        this.status = StoreRequestStatus.APPROVED;
    }
    
    public void reject() {
        this.status = StoreRequestStatus.REJECTED;
    }

    //삭제 요청용
    public static StoreRequest createDeleteRequest(Store store, String reason) {
        StoreRequest req = new StoreRequest();
        req.store = store;
        req.requestType = StoreRequestType.DELETE;
        req.store_desc = reason != null ? reason : "";
        req.status = StoreRequestStatus.PENDING;
        
        return req;
    }
    /* ?유저 권한 변경? */
}
