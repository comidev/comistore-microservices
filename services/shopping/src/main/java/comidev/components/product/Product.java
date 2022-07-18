package comidev.components.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {
    private Float price;
    private String name;
    private String photoUrl;
    private String description;
    private Integer stock;
}
