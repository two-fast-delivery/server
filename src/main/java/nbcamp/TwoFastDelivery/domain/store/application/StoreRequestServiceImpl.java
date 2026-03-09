package nbcamp.TwoFastDelivery.domain.store.application;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.domain.store.domain.Store;
import nbcamp.TwoFastDelivery.domain.store.domain.StoreRepository;
import nbcamp.TwoFastDelivery.domain.store.domain.StoreRequest;
import nbcamp.TwoFastDelivery.domain.store.domain.StoreRequestRepository;
import nbcamp.TwoFastDelivery.domain.store.domain.StoreRequestStatus;
import nbcamp.TwoFastDelivery.domain.store.domain.StoreRequestType;
import nbcamp.TwoFastDelivery.global.exception.CustomException;
import nbcamp.TwoFastDelivery.global.exception.ErrorCode;


@RequiredArgsConstructor
@Service
public class StoreRequestServiceImpl implements StoreRequestService {

    private final StoreRequestRepository storeRequestRepository;
    private final StoreRepository storeRepository;
    

    @Override
    public void requestDeleteStore(UUID storeId, CurrentUser user) {
        Store store = storeRepository.findByIdAndDeletedAtIsNull(storeId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        StoreAuthorizationService.validateOwnerOrManagerOrMaster(store, user);

        if(storeRequestRepository.existsByStore_IdAndRequestTypeAndStatus(storeId, StoreRequestType.DELETE, StoreRequestStatus.PENDING)){
            throw new CustomException(ErrorCode.CONFLICT);
        }
        
        StoreRequest deleteRequest = StoreRequest.createDeleteRequest(store,"");
        storeRequestRepository.save(deleteRequest);
    }
}
