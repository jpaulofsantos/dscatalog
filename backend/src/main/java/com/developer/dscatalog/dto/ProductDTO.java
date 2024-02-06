package com.developer.dscatalog.dto;

import com.developer.dscatalog.entities.Category;
import com.developer.dscatalog.entities.Product;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ProductDTO implements Serializable {

    private Long id;

    private String name;

    private String description;

    private Double price;

    private String imgUrl;

    private Instant date;

    private List<CategoryDTO> categories = new ArrayList<>();

    public ProductDTO() {

    }

    public ProductDTO(Long id, String name, String description, Double price, String imgUrl, Instant date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
        this.date = date;
    }

    public ProductDTO(Product product) {
        id = product.getId();
        name = product.getName();
        description = product.getDescription();
        price = product.getPrice();
        imgUrl = product.getImgUrl();
        date = product.getDate();
        for (Category category : product.getCategories()) {
            categories.add(new CategoryDTO(category));
        }
    }

    public ProductDTO(Product product, Set<Category> categories) {
        this(product);
        categories.forEach(category -> this.categories.add(new CategoryDTO(category)));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public Instant getDate() {
        return date;
    }

    public List<CategoryDTO> getCategories() {
        return categories;
    }
}
