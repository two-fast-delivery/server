package nbcamp.TwoFastDelivery.domain.store.application;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.domain.store.application.dto.StoreCreateRequest;
import nbcamp.TwoFastDelivery.domain.store.application.dto.StoreDetailResponse;
import nbcamp.TwoFastDelivery.domain.store.application.dto.StoreSearchCondition;
import nbcamp.TwoFastDelivery.domain.store.application.dto.StoreSummaryResponse;
import nbcamp.TwoFastDelivery.domain.store.application.dto.StoreUpdateRequest;
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


    @Override
    public void updateStore(UUID storeId, StoreUpdateRequest request, CurrentUser user) {
      Store store = storeRepository.findByIdAndDeletedAtIsNull(storeId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
      
      StoreAuthorizationService.validateOwnerOrManagerOrMaster(store, user);

      // 카테고리 확인
      if(request.getCategoryId() != null) {
        categoryRepository.findById(request.getCategoryId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        store.changeCategory(request.getCategoryId());
    }

      // 값이 null 이면 기존 정보 유지
      String name = request.getName() != null ? request.getName() : store.getName();
      String address = request.getAddress() != null ? request.getAddress() : store.getAddress();
      String phone = request.getPhone() != null ? request.getPhone() : store.getPhone();
      String thumbnailUrl = request.getThumbnailUrl() != null ? request.getThumbnailUrl() : store.getThumbnail_url();
      String openTime = request.getOpenTime() != null ? request.getOpenTime() : store.getOpen_time();
      String closeTime = request.getCloseTime() != null ? request.getCloseTime() : store.getClose_time();
      String description = request.getDescription() != null ? request.getDescription() : store.getDescription();


      store.changeBasicInfo(name, address, phone, thumbnailUrl, openTime, closeTime, description);

      storeRepository.save(store);
    }

    @Override
    public Page<StoreSummaryResponse> getStores(StoreSearchCondition condition, Pageable pageable, CurrentUser user){
      if(user.id()==null){
        throw new CustomException(ErrorCode.UNAUTHORIZED);
      }

      Page<Store> page = storeRepository.findByStatusAndDeletedAtIsNull(StoreStatus.OPEN, pageable);

      return page.map(store -> StoreSummaryResponse
        .builder()
        .id(store.getId())
        .name(store.getName())
        .address(store.getAddress())
        .thumbnailUrl(store.getThumbnail_url())
        .categoryName("")// TODO: 추가필요
        .avgRating(store.getAvg_rating())
        .reviewCount(store.getReview_count())
        .build());
    
    }

    @Override
    public List<StoreSummaryResponse> getMyStores(CurrentUser user){
      if(user.id()==null){
        throw new CustomException(ErrorCode.UNAUTHORIZED);
      }

      List<Store> stores = storeRepository.findByUserIdAndDeletedAtIsNull(user.id());

      return stores.stream().map(store -> StoreSummaryResponse
        .builder()
        .id(store.getId())
        .name(store.getName())
        .address(store.getAddress())
        .thumbnailUrl(store.getThumbnail_url())
        .categoryName("") // TODO: 추가필요
        .avgRating(store.getAvg_rating())
        .reviewCount(store.getReview_count())
        .build()).toList();


    }


  }

