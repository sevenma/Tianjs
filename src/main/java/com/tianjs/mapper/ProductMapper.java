package com.tianjs.mapper;

import java.util.List;

import com.tianjs.model.Product;

public interface ProductMapper {
    public Product findById(String id);
    public void insert(Product product);
    public List<Product> findPageBy(Product product);
}
