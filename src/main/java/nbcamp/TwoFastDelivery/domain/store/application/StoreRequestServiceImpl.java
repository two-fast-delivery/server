package nbcamp.TwoFastDelivery.domain.store.application;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.domain.store.application.dto.StoreRequestDecisionRequest;
import nbcamp.TwoFastDelivery.domain.store.domain.Store;
import nbcamp.TwoFastDelivery.domain.store.domain.StoreRepository;
import nbcamp.TwoFastDelivery.domain.store.domain.StoreRequest;
import nbcamp.TwoFastDelivery.domain.store.domain.StoreRequestRepository;
import nbcamp.TwoFastDelivery.domain.store.domain.StoreRequestStatus;
import nbcamp.TwoFastDelivery.domain.store.domain.StoreRequestType;
import nbcamp.TwoFastDelivery.domain.store.domain.StoreStatus;
import nbcamp.TwoFastDelivery.global.exception.CustomException;
import nbcamp.TwoFastDelivery.global.exception.ErrorCode;


@RequiredArgsConstructor
@Service
public class StoreRequestServiceImpl implements StoreRequestService {

    private final StoreRequestRepository storeRequestRepository;
    private final StoreRepository storeRepository;
    private final StoreAuthorizationService storeAuthorizationService;
    

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

    @Override
    public void decideRequest(UUID requestId, StoreRequestDecisionRequest request, CurrentUser admin){
        
        // 관리자 권한 체크
        if(!admin.hasRole("MASTER")){
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        // 요청 조회
        StoreRequest storeRequest = storeRequestRepository.findById(requestId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    
        // 중복 신청 
        if(storeRequest.getStatus()!=StoreRequestStatus.PENDING){
            throw new CustomException(ErrorCode.CONFLICT);
        }

        // 요청 처리리
        if(storeRequest.getRequestType()==StoreRequestType.REGISTRATION){
            handleRegistrationDecision(storeRequest, request.getApproved());
        }else if(storeRequest.getRequestType()==StoreRequestType.DELETE){
            handleDeleteDecision(storeRequest, request.getApproved());
        }else{
            throw new CustomException(ErrorCode.INTERNAL_ERROR);
        }
    }

    private void handleRegistrationDecision(StoreRequest storeRequest, boolean approved){
        Store store = storeRequest.getStore();

        if(approved){
            store.changeStatus(StoreStatus.OPEN);
            storeRequest.approve();
        }else{
            storeRequest.reject(); // PREPARING 유지
        }

        storeRepository.save(store);
        storeRequestRepository.save(storeRequest);
    }

    private void handleDeleteDecision(StoreRequest storeRequest, boolean approved){
        Store store = storeRequest.getStore();

        if(approved){
            store.softDelete();
            storeRequest.approve();
        }else{
            storeRequest.reject(); 
        }

        storeRepository.save(store);
        storeRequestRepository.save(storeRequest);
    }
}
