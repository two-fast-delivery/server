package nbcamp.TwoFastDelivery.domain.store.application;

import java.util.UUID;

public interface StoreRequestService {
   // void decideRequest(UUID requestId, StoreRequestDecisionRequest request, CurrentUser admin);
   void requestDeleteStore(UUID storeId, CurrentUser user);
}
