package com.developer.dscatalog.services;

import com.developer.dscatalog.dto.ProductDTO;
import com.developer.dscatalog.entities.Product;
import com.developer.dscatalog.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Page<ProductDTO> findAllPaged(Pageable pageable) {
        Page<Product> result = productRepository.findAll(pageable);
        return result.map(x-> new ProductDTO(x));
    }
}
