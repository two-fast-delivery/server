package nbcamp.TwoFastDelivery.domain.store.application;

import java.util.UUID;

import nbcamp.TwoFastDelivery.domain.store.application.dto.StoreCreateRequest;
import nbcamp.TwoFastDelivery.domain.store.application.dto.StoreDetailResponse;
import nbcamp.TwoFastDelivery.domain.store.application.dto.StoreUpdateRequest;




public interface StoreService {
     
    UUID createStore(StoreCreateRequest request, CurrentUser user);

    StoreDetailResponse getStore(UUID storeId, CurrentUser user);


    void updateStore(UUID storeId, StoreUpdateRequest request, CurrentUser user);
    /* 
    void requestDeleteStore(UUID storeId, CurrentUser user);

    Page<StoreSummaryResponse> getStores(StoreSearchCondition condition, Pageable pageable, CurrentUser user);

    List<StoreSummaryResponse> getMystores(CurrentUser user);

   */ 
}
