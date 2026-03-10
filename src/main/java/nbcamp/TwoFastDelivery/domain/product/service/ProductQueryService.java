package nbcamp.TwoFastDelivery.domain.product.service;

import lombok.RequiredArgsConstructor;
import nbcamp.TwoFastDelivery.global.exception.CustomException;
import nbcamp.TwoFastDelivery.global.exception.ErrorCode;
import nbcamp.TwoFastDelivery.domain.product.dto.response.ProductResponse;
import nbcamp.TwoFastDelivery.domain.product.repository.ProductQueryMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductQueryService {

    private final ProductQueryMapper productQueryMapper;

    public List<ProductResponse.Info> getProductsByStore(UUID storeId) {
        return productQueryMapper.getProductsByStore(storeId);
    }

    public ProductResponse.Info getProduct(UUID productId) {
        ProductResponse.Info productInfo = productQueryMapper.getProduct(productId);
        if (productInfo == null) {
            throw new CustomException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        return productInfo;
    }
}
