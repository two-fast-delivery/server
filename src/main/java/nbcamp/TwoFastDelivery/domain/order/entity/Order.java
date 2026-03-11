package nbcamp.TwoFastDelivery.domain.order.entity;

/*
* 0. 주문은 반드시 회원의 권한을 가진 사용자만 가능
*  - 입력 항목 : 가게ID, 상품리스트, 배송지, 요청사항
*  - 가게위치와 배송지주소가 광화문 근처여야 가능
* 1. 주문 상품이 1개 이상이어야 주문이 가능
* 2. 주문 상품은 주문이 가능한 상품인지 체크한다.
*  - 매장의 영업 여부 체크: Store::isVisible()
*  - 상품의 주문 가능 여부 체크: Product::isOrderable()
* 3. 주문은 한 가게에서만 가능하다.
*  - 장바구니도 한 가게에서만 가능하며 다른 가게의 상품을 담을 시 기존 가게 제품이 지워짐
* 4. 주문 상품의 총 금액은 주문상품 목록을 통해서만 계산
* 5. 주문 취소는 주문 접수 후 5분 이내에만 가능
*  - 입금 전 : 주문 취소(ORDER_CANCEL)
*  - 입금 후 : 주문 환불(ORDER_REFUND / 결제 취소 진행:이벤트)
* 6. 주문 상태 변경은 OWNER만 처리 가능
*  - 주문 요청 -> 조리중 -> 조리완료 -> 배달 중 -> 배달 완료
*  - 주문 거절은 '주문 요청' 상태에서만 가능
* 7. 배송 중 주문 상태는 입금 확인이 되어야만 변경 가능
* 8. 결제는 실제 연동 대신 데이터베이스 테이블에 성공 내역을 기록하여 처리
* 9. 배달완료(ORDER_DONE)으로 변경하면 리뷰 작성 요청 이벤트 발생 시킨다.
* - 검색 및 정렬
    - **정렬**: 최근 주문 순(기본값)
    - **키워드 검색** 지원
    - **기간별 필터링**: 최근 1일, 1주일, 1개월, 1년
    - **페이징**: 10(기본값) / 30 / 50 단위
* */

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nbcamp.TwoFastDelivery.global.common.BaseEntity;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

@Entity
@Getter
@Table(name = "p_order")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "store_id", nullable = false)
    private UUID storeId;

    @Embedded
    private Amount amount;

    @Column(nullable = false, length = 255)
    private String address;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(length = 100)
    private String req;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    @Builder
    public Order(UUID userId, UUID storeId, String address, Amount amount, String req, OrderStatus status) {
        this.userId = userId;
        this.storeId = storeId;
        this.address = address;
        this.amount = amount;
        this.req = req;
        this.status = status;
    }

    public void addOrderDetail(OrderDetail Detail) {
        this.orderDetails.add(Detail);
        Detail.setOrder(this);
    }

    public void cancel(){
        // 배달상태확인
        if(this.getStatus() == OrderStatus.IN_DELIVERY){
            throw new IllegalArgumentException("배달 중이므로 취소할 수 없습니다.");
        }else if(this.getStatus() == OrderStatus.DELIVERY_COMPLETED){
            throw new IllegalArgumentException(("배달 완료 상태이므로 취소할 수 없습니다."));
        }

        //상태변경
        this.status = OrderStatus.CANCELLED;
    }

    public void updateStatus(OrderStatus newStatus) {
        if(this.status == OrderStatus.CANCELLED){
            throw new IllegalStateException("취소건은 상태를 변경할 수 없습니다.");
        }else if(this.status == OrderStatus.DELIVERY_COMPLETED){
            throw new IllegalStateException("배달 완료된 주문은 상태를 변경할 수 없습니다.");
        }

        this.status = newStatus;
    }

}