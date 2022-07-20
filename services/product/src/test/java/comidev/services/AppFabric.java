package comidev.services;

import org.springframework.stereotype.Service;

import java.util.UUID;

import comidev.components.category.Category;
import comidev.components.category.CategoryRepo;
import comidev.components.product.Product;
import comidev.components.product.ProductRepo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Service
@Getter
@AllArgsConstructor
public class AppFabric {
    private final CategoryRepo categoryRepo;
    private final ProductRepo productRepo;

    private String generate() {
        return UUID.randomUUID().toString();
    }

    public Category createCategory(String name) {
        String nameDB = name != null ? name : generate();
        return categoryRepo.save(new Category(nameDB));
    }

    public Product createProduct(String name) {
        String nameDB = name != null ? name : "name";
        return productRepo.save(new Product(nameDB, "x",
                "x", 10, 3.5f));
    }
}
