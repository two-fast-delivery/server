package nbcamp.TwoFastDelivery.domain.store.application;

import java.util.UUID;

import nbcamp.TwoFastDelivery.domain.store.application.dto.StoreRequestDecisionRequest;

public interface StoreRequestService {

   void decideRequest(UUID requestId, StoreRequestDecisionRequest request, CurrentUser admin);
   void requestDeleteStore(UUID storeId, CurrentUser user);

}
