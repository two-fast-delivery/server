package nbcamp.TwoFastDelivery.domain.product.controller;

import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.domain.product.dto.request.ProductRequest;
import nbcamp.TwoFastDelivery.domain.product.dto.response.ProductResponse;
import nbcamp.TwoFastDelivery.domain.product.service.ProductQueryService;
import nbcamp.TwoFastDelivery.domain.product.service.ProductService;
import nbcamp.TwoFastDelivery.global.common.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
        return CommonResponse.success("상품 목록 조회 성공", productQueryService.getProductsByStore(storeId));
    }

    @GetMapping("/stores/{storeId}/products/{productId}")
    public CommonResponse<ProductResponse.Info> getProduct(
            @PathVariable UUID storeId,
            @PathVariable UUID productId) {
        return CommonResponse.success("상품 상세 조회 성공", productQueryService.getProduct(productId));
    }

    // [OWNER]
    @GetMapping("/owner/stores/{storeId}/products")
    public CommonResponse<List<ProductResponse.Info>> getOwnerProductsByStore(
            @PathVariable UUID storeId) {
        return CommonResponse.success("상품 목록 조회 성공", productQueryService.getProductsByStore(storeId));
    }

    @PostMapping("/owner/stores/{storeId}/products")
    @ResponseStatus(HttpStatus.CREATED)
    public CommonResponse<ProductResponse.Info> createProduct(
            @PathVariable UUID storeId,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ProductRequest.Create request) {
        String username = userDetails.getUsername();
        return CommonResponse.success("상품 생성 성공", productService.createProduct(storeId, username, request));
    }

    @PatchMapping("/owner/stores/{storeId}/products/{productId}")
    public CommonResponse<ProductResponse.Info> updateProduct(
            @PathVariable UUID storeId,
            @PathVariable UUID productId,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ProductRequest.Update request) {
        String username = userDetails.getUsername();
        return CommonResponse.success("상품 수정 성공", productService.updateProduct(storeId, productId, username, request));
    }

    @DeleteMapping("/owner/stores/{storeId}/products/{productId}")
    public CommonResponse<Void> deleteProduct(
            @PathVariable UUID storeId,
            @PathVariable UUID productId,
            @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        productService.deleteProduct(storeId, productId, username);
        return CommonResponse.success("상품 삭제 성공");
    }

    @PostMapping("/owner/stores/{storeId}/gen-description")
    public CommonResponse<String> generateProductDescriptionAI(
            @PathVariable UUID storeId,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ProductRequest.GenerateDescription request) {
        String username = userDetails.getUsername();
        String generatedDescription = productService.generateProductDescriptionPreview(request.getPrompt(),
                request.getImageUrl(), storeId.toString(), username);
        return CommonResponse.success("상품 설명이 생성되었습니다.", generatedDescription);
    }

    // [ADMIN]
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin/stores/{storeId}/products/{productId}")
    public CommonResponse<String> requestProductModification(
            @PathVariable UUID storeId,
            @PathVariable UUID productId,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody Object reasonDummy) {

        productService.requestProductModification(productId, reasonDummy);

        return CommonResponse.success("판매자에게 상품 수정 요구가 성공적으로 전달되었습니다.", "성공");
    }

}
