package nbcamp.TwoFastDelivery.domain.store.domain;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
    @Table(name = "p_store")
    public class Store {
        @Id
        @Column(name = "id", nullable = false, columnDefinition = "uuid")
        private UUID id;
        
        @Column(name = "user_id", nullable = false, columnDefinition = "uuid")
        private UUID user_id;

        @Column(name = "name", nullable = false)
        private String name;
        
        @Column(name = "address", nullable = false)
        private String address;
        
        @Column(name = "phone", nullable = false)
        private String phone;

        @Column(name = "category_id", nullable = false, columnDefinition = "uuid")
        private UUID category_id;

        @Column(name = "thumbnail_url")
        private String thumbnail_url;

        @Column(name = "open_time", nullable = false)
        private String open_time;

        @Column(name = "close_time", nullable = false)
        private String close_time;

        @Column(name = "description")
        private String description;

        @Column(name = "status", nullable = false)
        private StoreStatus status;

        @Column(name = "avg_rating", columnDefinition = "tinyint default 0")
        private Byte avg_rating;

        @Column(name = "review_count", columnDefinition = "int default 0")
        private Integer review_count;


        // 기본 정보 변경
        public void changeBasicInfo(String name, String address, String phone, String thumbnail_url, String open_time, String close_time, String description) {
            this.name = name;
            this.address = address;
            this.phone = phone;
            this.thumbnail_url = thumbnail_url;
            this.open_time = open_time;
            this.close_time = close_time;
            this.description = description;
        }


        // 상태 변경
        public void changeStatus(StoreStatus status) {
            this.status = status;
        }
        
        @PrePersist
        public void prePersist() {
            if (id == null) {
                id = UUID.randomUUID();
            }
        }
}