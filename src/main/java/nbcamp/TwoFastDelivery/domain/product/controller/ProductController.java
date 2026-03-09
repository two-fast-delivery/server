package nbcamp.TwoFastDelivery.domain.product.controller;

import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.domain.product.dto.request.ProductRequest;
import nbcamp.TwoFastDelivery.domain.product.dto.response.ProductResponse;
import nbcamp.TwoFastDelivery.domain.product.service.ProductQueryService;
import nbcamp.TwoFastDelivery.domain.product.service.ProductService;
import nbcamp.TwoFastDelivery.global.common.CommonResponse;
import nbcamp.TwoFastDelivery.global.exception.CustomException;
import nbcamp.TwoFastDelivery.global.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductQueryService productQueryService;

    // [ALL]

    @GetMapping("/stores/{storeId}/products")
    public CommonResponse<List<ProductResponse.Info>> getProductsByStore(@PathVariable UUID storeId) {
        return CommonResponse.success(productQueryService.getProductsByStore(storeId));
    }

    @GetMapping("/stores/{storeId}/products/{productId}")
    public CommonResponse<ProductResponse.Info> getProduct(
            @PathVariable UUID storeId,
            @PathVariable UUID productId) {
        return CommonResponse.success(productQueryService.getProduct(productId));
    }

    // [OWNER]
    @GetMapping("/owner/stores/{storeId}/products")
    public CommonResponse<List<ProductResponse.Info>> getOwnerProductsByStore(
            @PathVariable UUID storeId) {
        return CommonResponse.success(productQueryService.getProductsByStore(storeId));
    }

    @PostMapping("/owner/stores/{storeId}/products")
    @ResponseStatus(HttpStatus.CREATED)
    public CommonResponse<ProductResponse.Info> createProduct(
            @PathVariable UUID storeId,
            @RequestBody ProductRequest.Create request) {
        return CommonResponse.success(productService.createProduct(storeId, request));
    }

    @PatchMapping("/owner/stores/{storeId}/products/{productId}")
    public CommonResponse<ProductResponse.Info> updateProduct(
            @PathVariable UUID storeId,
            @PathVariable UUID productId,
            @RequestBody ProductRequest.Update request) {
        return CommonResponse.success(productService.updateProduct(productId, request));
    }

    @DeleteMapping("/owner/stores/{storeId}/products/{productId}")
    public CommonResponse<Void> deleteProduct(
            @PathVariable UUID storeId,
            @PathVariable UUID productId,
            @RequestHeader(value = "X-User-Id", defaultValue = "owner-system") String deletedBy) {
        productService.deleteProduct(productId, deletedBy);
        return CommonResponse.success();
    }

    @PostMapping("/owner/stores/{storeId}/gen-description")
    public CommonResponse<String> generateProductDescriptionAI(
            @PathVariable UUID storeId,
            @RequestBody ProductRequest.GenerateDescription request) {
        String generatedDescription = productService.generateProductDescriptionPreview(request.getPrompt(),
                request.getImageUrl());
        return CommonResponse.success("상품 설명이 생성되었습니다.", generatedDescription);
    }

    // [ADMIN]
    @PostMapping("/admin/stores/{storeId}/products/{productId}")
    public CommonResponse<String> requestProductModification(
            @PathVariable UUID storeId,
            @PathVariable UUID productId,
            @RequestHeader(value = "X-Role", defaultValue = "USER") String role,
            @RequestBody Object reasonDummy) {

        // Admin 권한 확인
        if (!"ADMIN".equalsIgnoreCase(role)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        productService.requestProductModification(productId, reasonDummy);

        return CommonResponse.success("판매자에게 상품 수정 요구가 성공적으로 전달되었습니다.", "성공");
    }

}
