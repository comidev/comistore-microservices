package comidev.components.product;

import org.springframework.stereotype.Service;

import comidev.components.product.dto.Stock;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductFeign productFeign;

    public Product updateStock(Long id, Integer stock) {
        return productFeign.updateStock(id, new Stock(stock));
    }
}
