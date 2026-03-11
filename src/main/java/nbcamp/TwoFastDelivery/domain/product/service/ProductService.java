package nbcamp.TwoFastDelivery.domain.product.service;

import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.domain.product.dto.request.ProductRequest;
import nbcamp.TwoFastDelivery.domain.product.dto.response.ProductResponse;
import nbcamp.TwoFastDelivery.domain.product.entity.Product;
import nbcamp.TwoFastDelivery.domain.product.entity.ProductGroup;
import nbcamp.TwoFastDelivery.global.exception.CustomException;
import nbcamp.TwoFastDelivery.global.exception.ErrorCode;
import nbcamp.TwoFastDelivery.domain.product.repository.ProductGroupRepository;
import nbcamp.TwoFastDelivery.domain.product.repository.ProductRepository;
import nbcamp.TwoFastDelivery.infrastructure.ai.GeminiClient;
import nbcamp.TwoFastDelivery.domain.store.domain.Store;
import nbcamp.TwoFastDelivery.domain.store.domain.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductGroupRepository productGroupRepository;
    private final StoreRepository storeRepository;
    private final GeminiClient geminiClient;

    /**
     * 새로운 상품을 생성합니다.
     * 상품 생성 시 요청된 그룹 정보를 확인하여 기존 그룹을 연결하거나 새로운 그룹을 생성하여 연결합니다.
     *
     * @param storeId 상품을 생성할 상점의 ID
     * @param request 상품 생성 요청 정보 (이름, 설명, 가격, 재고 등)
     * @return 생성된 상품의 상세 정보
     */
    @Transactional
    public ProductResponse.Info createProduct(UUID storeId, UUID userId, ProductRequest.Create request) {
        // TODO: JWT 미완성으로 인한 주석 처리
        // validateStoreOwnershipForCreation(storeId, userId);
        UUID groupId = resolveProductGroupId(storeId, request.getProductGroupId(), request.getProductGroupName());

        String finalDescription = request.getDescription();
        String prompt = request.getPrompt();
        if (Boolean.TRUE.equals(request.getIsAiGenerated())) {
            validatePromptLength(prompt);
            finalDescription = geminiClient.generateDescription(prompt, request.getImageUrl(), storeId.toString());
        }

        Product product = Product.builder()
                .storeId(storeId) // From controller path or auth
                .productGroupId(groupId)
                .name(request.getName())
                .description(finalDescription)
                .prompt(prompt)
                .imageUrl(request.getImageUrl())
                .price(request.getPrice())
                .stock(request.getStock())
                .maxOrderQuantity(request.getMaxOrderQuantity())
                .sortOrder(request.getSortOrder())
                .isAiGenerated(request.getIsAiGenerated())
                .status(Product.ProductStatus.SALE) // 기본 상태
                .build();

        return mapToResponse(productRepository.save(product));
    }

    /**
     * 특정 상점의 모든 상품 목록을 조회합니다.
     * 삭제된 상품은 제외되며, 각 상품이 속한 그룹의 이름도 함께 조회하여 응답에 포함합니다.
     * 
     * @deprecated CQRS 패턴 적용으로 인하여 조회 로직은 {@link ProductQueryService} 로 분리되었습니다. 더
     *             이상 사용하지 않습니다.
     * @param storeId 상품을 조회할 상점의 ID
     * @return 해당 상점에 등록된 상품 목록
     */
    @Deprecated
    @Transactional(readOnly = true)
    public List<ProductResponse.Info> getProductsByStore(UUID storeId) {
        List<Product> products = productRepository.findByStoreIdAndDeletedAtIsNull(storeId);
        List<ProductGroup> groups = productGroupRepository.findByStoreIdAndDeletedAtIsNull(storeId);
        Map<UUID, String> groupNameMap = groups.stream()
                .collect(Collectors.toMap(ProductGroup::getId, ProductGroup::getName));

        return products.stream()
                .map(p -> ProductResponse.Info.fromEntity(p,
                        p.getProductGroupId() != null ? groupNameMap.get(p.getProductGroupId()) : null))
                .collect(Collectors.toList());
    }

    /**
     * 상품 ID를 통해 단일 상품의 상세 정보를 조회합니다.
     *
     * @deprecated CQRS 패턴 적용으로 인하여 조회 로직은 {@link ProductQueryService} 로 분리되었습니다. 더
     *             이상 사용하지 않습니다.
     * @param productId 조회할 상품의 ID
     * @return 단일 상품의 상세 정보
     */
    @Deprecated
    @Transactional(readOnly = true)
    public ProductResponse.Info getProduct(UUID productId) {
        Product product = getProductEntity(productId);
        return mapToResponse(product);
    }

    /**
     * 기존 상품의 정보를 수정합니다.
     * 상품 그룹 정보, 이름, 설명, 가격 등 전달받은 데이터로 기존 정보를 덮어씁니다.
     *
     * @param productId 수정할 상품의 ID
     * @param request   수정할 상품 정보
     * @return 수정이 완료된 상품의 상세 정보
     */
    @Transactional
    public ProductResponse.Info updateProduct(UUID storeId, UUID productId, UUID userId, ProductRequest.Update request) {
        Product product = getProductEntity(productId);
        validateStoreOwnership(product, storeId, userId);
        UUID groupId = resolveProductGroupId(product.getStoreId(), request.getProductGroupId(),
                request.getProductGroupName());

        String finalDescription = request.getDescription();
        String prompt = request.getPrompt();
        if (Boolean.TRUE.equals(request.getIsAiGenerated())) {
            validatePromptLength(prompt);
            finalDescription = geminiClient.generateDescription(prompt, request.getImageUrl(), storeId.toString());
        }

        product.updateInfo(groupId, request.getName(), finalDescription, prompt,
                request.getImageUrl(), request.getPrice(), request.getStock(),
                request.getMaxOrderQuantity(), request.getSortOrder(), request.getIsAiGenerated());
        return mapToResponse(product);
    }

    /**
     * 상품의 판매 상태(예: SALE, SOLD_OUT, HIDDEN 등)를 변경합니다.
     *
     * @param productId 상태를 변경할 상품의 ID
     * @param request   변경할 상태 정보
     * @return 상태가 변경된 상품의 상세 정보
     */
    @Transactional
    public ProductResponse.Info changeProductStatus(UUID storeId, UUID productId, UUID userId, ProductRequest.ChangeStatus request) {
        Product product = getProductEntity(productId);
        validateStoreOwnership(product, storeId, userId);
        product.changeStatus(Product.ProductStatus.valueOf(request.getStatus()));
        return mapToResponse(product);
    }

    /**
     * [관리자 전용] 상품 수정 요구 (상품을 HIDDEN 상태로 변경)
     */
    @Transactional
    public void requestProductModification(UUID productId, Object reasonDummy) {
        Product product = getProductEntity(productId);

        // 상품 상태를 HIDDEN으로 강제 변경
        product.changeStatus(Product.ProductStatus.HIDDEN);
    }

    /**
     * 상품을 삭제합니다. (Soft Delete 방식)
     * 데이터베이스에서 물리적으로 삭제하지 않고, 삭제 시간과 삭제자를 기록하여 논리적으로만 삭제 처리합니다.
     *
     * @param productId 삭제 처리할 상품의 ID
     * @param deletedBy 삭제를 수행한 사용자의 식별자 (또는 이름)
     */
    @Transactional
    public void deleteProduct(UUID storeId, UUID productId, UUID userId) {
        Product product = getProductEntity(productId);
        validateStoreOwnership(product, storeId, userId);
        // Soft Delete
        product.delete(userId);

        // 옵션 그룹 처리와 동일하게, 이 상품이 속한 카테고리(그룹)에 남은 다른 안 지워진 상품이 없으면 그룹도 삭제
        if (product.getProductGroupId() != null) {
            boolean hasOtherProducts = productRepository.existsByProductGroupIdAndDeletedAtIsNullAndIdNot(
                    product.getProductGroupId(), productId);

            if (!hasOtherProducts) {
                productGroupRepository.findByIdAndDeletedAtIsNull(product.getProductGroupId())
                        .ifPresent(group -> group.delete(userId));
            }
        }
    }

    /**
     * 상품 ID로 삭제되지 않은 유효한 상품 엔티티를 조회합니다.
     * 상품이 존재하지 않거나 이미 삭제된 경우 예외를 발생시킵니다.
     *
     * @param productId 조회할 상품의 ID
     * @return 조회된 상품 엔티티
     * @throws IllegalArgumentException 상품을 찾을 수 없을 때 발생
     */
    private Product getProductEntity(UUID productId) {
        return productRepository.findByIdAndDeletedAtIsNull(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    /**
     * 상품이 요청한 상점의 것이며, 해당 상점이 요청한 유저의 소유인지 확인합니다.
     *
     * @param product 확인할 상품 엔티티
     * @param storeId 요청한 상점 ID
     * @param userId 요청한 유저 ID
     */
    private void validateStoreOwnership(Product product, UUID storeId, UUID userId) {
        if (!product.getStoreId().equals(storeId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
        
        Store store = storeRepository.findByIdAndDeletedAtIsNull(storeId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
            
        if (!store.getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }

    /**
     * 상점이 요청한 유저의 소유인지 확인합니다. (새 상품 생성 시 사용)
     */
    /* TODO: JWT 미완성으로 인한 주석 처리
    private void validateStoreOwnershipForCreation(UUID storeId, UUID userId) {
        Store store = storeRepository.findByIdAndDeletedAtIsNull(storeId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
            
        if (!store.getUserId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }
    */

    /**
     * 상품 등록 및 수정 시 필요한 상품 그룹 ID를 결정합니다.
     * 1. 요청된 그룹 ID가 명시적으로 있다면 해당 ID를 사용합니다.
     * 2. 그룹 이름만 요청된 경우, 해당 이름의 그룹이 존재하는지 조회하여 재사용하거나 없으면 새로 생성하여 그 ID를 반환합니다.
     * 3. 둘 다 없으면 그룹 없이 null을 반환합니다.
     *
     * @param storeId            상점 ID
     * @param requestedGroupId   요청으로 들어온 상품 그룹 ID (nullable)
     * @param requestedGroupName 요청으로 들어온 상품 그룹 이름 (nullable)
     * @return 결정된 상품 그룹의 ID (그룹이 없으면 null)
     */
    private UUID resolveProductGroupId(UUID storeId, UUID requestedGroupId, String requestedGroupName) {
        if (requestedGroupId != null) {
            return requestedGroupId;
        }
        if (requestedGroupName != null && !requestedGroupName.trim().isEmpty()) {
            Optional<ProductGroup> existingGroup = productGroupRepository
                    .findByNameAndStoreIdAndDeletedAtIsNull(requestedGroupName, storeId);
            if (existingGroup.isPresent()) {
                return existingGroup.get().getId();
            } else {
                // Create new group
                ProductGroup newGroup = ProductGroup.builder()
                        .storeId(storeId)
                        .name(requestedGroupName)
                        .build();
                productGroupRepository.save(newGroup);
                return newGroup.getId();
            }
        }
        return null;
    }

    /**
     * 상품 엔티티 객체를 클라이언트 응답용 DTO(ProductResponse.Info)로 변환합니다.
     * 상품의 그룹 ID가 존재하는 경우 데이터베이스에서 그룹 이름을 함께 조회하여 세팅합니다.
     *
     * @param product 변환할 상품 엔티티
     * @return 변환된 상품 정보 응답 객체
     */
    private ProductResponse.Info mapToResponse(Product product) {
        String groupName = null;
        if (product.getProductGroupId() != null) {
            groupName = productGroupRepository.findByIdAndDeletedAtIsNull(product.getProductGroupId())
                    .map(ProductGroup::getName)
                    .orElse(null);
        }
        return ProductResponse.Info.fromEntity(product, groupName);
    }

    public String generateProductDescriptionPreview(String prompt, String imageUrl, String storeId, UUID userId) {
        // TODO: JWT 미완성으로 인한 주석 처리
        // validateStoreOwnershipForCreation(UUID.fromString(storeId), userId);
        validatePromptLength(prompt);
        return geminiClient.generateDescription(prompt, imageUrl, storeId);
    }

    private void validatePromptLength(String prompt) {
        if (prompt != null && prompt.length() > 100) {
            throw new CustomException(ErrorCode.INVALID_PRODUCT_DATA);
        }
    }
}
