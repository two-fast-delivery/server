package nbcamp.TwoFastDelivery.domain.product.controller;

import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.domain.product.dto.request.ProductOptionRequest;
import nbcamp.TwoFastDelivery.domain.product.dto.response.ProductOptionResponse;
import nbcamp.TwoFastDelivery.domain.product.service.ProductOptionService;
import nbcamp.TwoFastDelivery.global.common.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ProductOptionController {

    private final ProductOptionService optionService;

    @PostMapping("/owner/stores/{storeId}/products/{productId}/options")
    @ResponseStatus(HttpStatus.CREATED)
    public CommonResponse<ProductOptionResponse.OptionInfo> createOption(
            @PathVariable UUID storeId,
            @PathVariable UUID productId,
            @RequestBody ProductOptionRequest.CreateOption request) {
        return CommonResponse.success("옵션 생성 성공", optionService.createOption(productId, request));
    }

    @PatchMapping("/owner/stores/{storeId}/products/{productId}/options/{optionId}")
    public CommonResponse<ProductOptionResponse.OptionInfo> updateOption(
            @PathVariable UUID storeId,
            @PathVariable UUID productId,
            @PathVariable UUID optionId,
            @RequestBody ProductOptionRequest.CreateOption request) {
        return CommonResponse.success("옵션 수정 성공", optionService.updateOption(optionId, request));
    }

    @DeleteMapping("/owner/stores/{storeId}/products/{productId}/options/{optionId}")
    public CommonResponse<Void> deleteOption(
            @PathVariable UUID storeId,
            @PathVariable UUID productId,
            @PathVariable UUID optionId,
            @RequestHeader(value = "X-User-Id") UUID deletedBy) {
        optionService.deleteOption(optionId, deletedBy);
        return CommonResponse.success("옵션 삭제 성공");
    }
}
