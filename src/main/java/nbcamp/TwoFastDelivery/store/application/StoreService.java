package nbcamp.TwoFastDelivery.store.application;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import nbcamp.TwoFastDelivery.store.application.dto.StoreCreateRequest;
import nbcamp.TwoFastDelivery.store.application.dto.StoreDetailResponse;
import nbcamp.TwoFastDelivery.store.application.dto.StoreSearchCondition;
import nbcamp.TwoFastDelivery.store.application.dto.StoreSummaryResponse;
import nbcamp.TwoFastDelivery.store.application.dto.StoreUpdateRequest;




public interface StoreService {
     
    UUID createStore(StoreCreateRequest request, CurrentUser user);

    StoreDetailResponse getStore(UUID storeId, CurrentUser user);


    void updateStore(UUID storeId, StoreUpdateRequest request, CurrentUser user);
    
    //void requestDeleteStore(UUID storeId, CurrentUser user);

    Page<StoreSummaryResponse> getStores(StoreSearchCondition condition, Pageable pageable, CurrentUser user);

    List<StoreSummaryResponse> getMyStores(CurrentUser user);

   
}
