package comidev.components.product;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import comidev.components.product.dto.Stock;

@FeignClient(name = "products", path = "/products")
public interface ProductFeign {

    @PatchMapping("/{id}/stock")
    public Product updateStock(@PathVariable(name = "id") Long id, Stock stock);
}
