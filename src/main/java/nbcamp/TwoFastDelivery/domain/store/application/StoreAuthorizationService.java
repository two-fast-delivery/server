// USER DTO 보고 수정 

package nbcamp.TwoFastDelivery.domain.store.application;

import org.springframework.stereotype.Service;
import nbcamp.TwoFastDelivery.domain.store.domain.Store;
import nbcamp.TwoFastDelivery.global.exception.CustomException;
import nbcamp.TwoFastDelivery.global.exception.ErrorCode;

@Service
public class StoreAuthorizationService {

    public static void validateOwnerOrManagerOrMaster(Store store, CurrentUser user) {
        boolean isOwner = store.getUserId().equals(user.id());
        boolean isManager = user.hasRole("MANAGER");
        boolean isMaster = user.hasRole("MASTER");

        if (!(isOwner || isManager || isMaster)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }
}


