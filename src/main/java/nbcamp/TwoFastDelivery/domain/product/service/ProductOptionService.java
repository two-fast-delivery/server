package nbcamp.TwoFastDelivery.domain.product.service;

import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.domain.product.dto.request.ProductOptionRequest;
import nbcamp.TwoFastDelivery.domain.product.dto.response.ProductOptionResponse;
import nbcamp.TwoFastDelivery.domain.product.entity.ProductOption;
import nbcamp.TwoFastDelivery.domain.product.entity.ProductOptionGroup;
import nbcamp.TwoFastDelivery.global.exception.CustomException;
import nbcamp.TwoFastDelivery.global.exception.ErrorCode;
import nbcamp.TwoFastDelivery.domain.product.repository.ProductOptionGroupRepository;
import nbcamp.TwoFastDelivery.domain.product.repository.ProductOptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductOptionService {

    private final ProductOptionGroupRepository optionGroupRepository;
    private final ProductOptionRepository optionRepository;
    private final ProductService productService;

    @Transactional
    public ProductOptionResponse.OptionInfo createOption(UUID storeId, UUID productId, String username, ProductOptionRequest.CreateOption request) {
        UUID userId = productService.resolveUserId(username);
        // 권한 확인
        productService.validateStoreOwnership(productService.getProductEntity(productId), storeId, userId);

        // 그룹 이름이 제공된 경우, 기존 그룹을 찾거나 새로 생성
        ProductOptionGroup group;
        if (request.getGroupName() != null && !request.getGroupName().isEmpty()) {
            group = optionGroupRepository.findByProductIdAndNameAndDeletedAtIsNull(productId, request.getGroupName())
                    .orElseGet(() -> optionGroupRepository.save(ProductOptionGroup.builder()
                            .productId(productId)
                            .name(request.getGroupName())
                            .minSelection(request.getMinSelection() != null ? request.getMinSelection() : 0)
                            .maxSelection(request.getMaxSelection() != null ? request.getMaxSelection() : 1)
                            .sortOrder(request.getSortOrder())
                            .build()));
        } else {
            throw new CustomException(ErrorCode.INVALID_PRODUCT_DATA);
        }

        ProductOption option = ProductOption.builder()
                .groupId(group.getId())
                .name(request.getName())
                .extraPrice(request.getExtraPrice())
                .stock(request.getStock())
                .sortOrder(request.getSortOrder())
                .status(ProductOption.ProductOptionStatus.AVAILABLE)
                .build();
        return ProductOptionResponse.OptionInfo.fromEntity(optionRepository.save(option));
    }

    @Transactional
    public ProductOptionResponse.OptionInfo updateOption(UUID storeId, UUID productId, UUID optionId, String username, ProductOptionRequest.CreateOption request) {
        UUID userId = productService.resolveUserId(username);
        ProductOption option = getOptionEntity(optionId);
        
        // 권한 확인
        productService.validateStoreOwnership(productService.getProductEntity(productId), storeId, userId);

        // 함께 연결된 옵션 그룹 수정 로직
        if (request.getGroupName() != null && !request.getGroupName().isEmpty()) {
            ProductOptionGroup group = getOptionGroupEntity(option.getGroupId());
            group.updateInfo(
                    request.getGroupName(),
                    request.getMinSelection() != null ? request.getMinSelection() : group.getMinSelection(),
                    request.getMaxSelection() != null ? request.getMaxSelection() : group.getMaxSelection(),
                    group.getSortOrder());
        }

        option.updateInfo(option.getGroupId(), request.getName(), request.getExtraPrice(), request.getStock(),
                request.getSortOrder());

        return ProductOptionResponse.OptionInfo.fromEntity(option);
    }

    @Transactional
    public void deleteOption(UUID storeId, UUID productId, UUID optionId, String username) {
        UUID userId = productService.resolveUserId(username);
        ProductOption option = getOptionEntity(optionId);
        
        // 권한 확인
        productService.validateStoreOwnership(productService.getProductEntity(productId), storeId, userId);

        option.delete(userId);

        // 방금 삭제한 옵션을 제외하고, 부모 그룹에 다른 '유효한(삭제되지 않은)' 옵션이 남아있는지 확인
        boolean hasOtherOptions = optionRepository.existsByGroupIdAndDeletedAtIsNullAndIdNot(option.getGroupId(),
                optionId);

        if (!hasOtherOptions) {
            // 다른 옵션이 없다면 부모 옵션 그룹도 함께 삭제
            ProductOptionGroup group = getOptionGroupEntity(option.getGroupId());
            group.delete(userId);
        }
    }

    private ProductOptionGroup getOptionGroupEntity(UUID id) {
        return optionGroupRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_OPTION_GROUP_NOT_FOUND));
    }

    private ProductOption getOptionEntity(UUID id) {
        return optionRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_OPTION_NOT_FOUND));
    }
}
