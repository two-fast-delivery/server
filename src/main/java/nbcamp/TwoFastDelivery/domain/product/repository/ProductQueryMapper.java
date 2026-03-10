package nbcamp.TwoFastDelivery.domain.product.repository;

import nbcamp.TwoFastDelivery.domain.product.dto.response.ProductResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.UUID;

@Mapper
public interface ProductQueryMapper {
    List<ProductResponse.Info> getProductsByStore(UUID storeId);

    ProductResponse.Info getProduct(UUID productId);
}
