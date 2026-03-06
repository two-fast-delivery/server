package nbcamp.TwoFastDelivery.domain.store.application;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.domain.store.application.dto.StoreCreateRequest;
import nbcamp.TwoFastDelivery.domain.store.application.dto.StoreDetailResponse;
import nbcamp.TwoFastDelivery.domain.store.domain.Category;
import nbcamp.TwoFastDelivery.domain.store.domain.CategoryRepository;
import nbcamp.TwoFastDelivery.domain.store.domain.Store;
import nbcamp.TwoFastDelivery.domain.store.domain.StoreRepository;
import nbcamp.TwoFastDelivery.domain.store.domain.StoreRequest;
import nbcamp.TwoFastDelivery.domain.store.domain.StoreRequestRepository;
import nbcamp.TwoFastDelivery.domain.store.domain.StoreStatus;
import nbcamp.TwoFastDelivery.global.exception.CustomException;
import nbcamp.TwoFastDelivery.global.exception.ErrorCode;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {
     
    private final StoreRepository storeRepository;
    private final StoreRequestRepository storeRequestRepository;
    private final CategoryRepository categoryRepository;
        

    @Override
    public UUID createStore(StoreCreateRequest request, CurrentUser user) {
      Category category = categoryRepository.findById(request.getCategoryId())
      .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

      Store store = Store.create(user.id(),
                                request.getName(),
                                request.getAddress(),
                                request.getPhone(),
                                request.getCategoryId(),
                                request.getThumbnailUrl(),
                                request.getOpenTime(),
                                request.getCloseTime(),
                                request.getDescription()
                              );

      storeRepository.save(store);

      String desc = request.getStoreDesc() != null ? request.getStoreDesc() : "";
      StoreRequest storeRequest = StoreRequest.createStoreRequest(store, desc);
      storeRequestRepository.save(storeRequest);

      return store.getId();
    }


    @Override
    public StoreDetailResponse getStore(UUID storeId, CurrentUser user) {

      // 가게 조회
      Store store = storeRepository.findByIdAndDeletedAtIsNull(storeId)
      .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
                           
      // 노출 권한 확인
      if(store.getStatus() != StoreStatus.OPEN) {
        boolean allowed = store.getUserId().equals(user.id()) || user.hasRole("OWNER");
        if(!allowed) {
          throw new CustomException(ErrorCode.FORBIDDEN);
        }
      }

      // 카테고리 조회
      String categoryName = categoryRepository.findById(store.getCategory_id())
                                              .map(Category::getName)
                                              .orElse("");


      return StoreDetailResponse.builder()
                                .id(store.getId())
                                .userId(store.getUserId())
                                .name(store.getName())
                                .address(store.getAddress())
                                .phone(store.getPhone())
                                .categoryId(store.getCategory_id())
                                .categoryName(categoryName)
                                .thumbnailUrl(store.getThumbnail_url())
                                .openTime(store.getOpen_time())
                                .closeTime(store.getClose_time())
                                .description(store.getDescription())
                                .status(store.getStatus().name())
                                .avgRating(store.getAvg_rating())
                                .reviewCount(store.getReview_count())
                                .build();
    }
}
