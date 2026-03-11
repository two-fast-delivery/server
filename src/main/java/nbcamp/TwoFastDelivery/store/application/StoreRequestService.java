package nbcamp.TwoFastDelivery.store.application;

import java.util.UUID;

import nbcamp.TwoFastDelivery.store.application.dto.StoreRequestDecisionRequest;

public interface StoreRequestService {

   void decideRequest(UUID requestId, StoreRequestDecisionRequest request, CurrentUser admin);
   void requestDeleteStore(UUID storeId, CurrentUser user);

}
