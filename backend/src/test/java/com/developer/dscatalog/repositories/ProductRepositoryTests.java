package com.developer.dscatalog.repositories;

import com.developer.dscatalog.entities.Product;
import com.developer.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    private Long existingId;

    private Long notExistingId;
    private Long countTotalProducts;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        notExistingId = 1000L;
        countTotalProducts = 25L;
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {

        productRepository.deleteById(existingId);

        Optional<Product> result = productRepository.findById(existingId);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {

        Product product = Factory.createProduct();

        product.setId(null);
        product = productRepository.save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProducts+1, product.getId());

    }

    @Test
    public void findByIdShouldReturnOptionalNotEmptyWhenIdExists() {
        Optional<Product> product = productRepository.findById(existingId);
        Assertions.assertTrue(product.isPresent());

    }

    @Test
    public void findByIdShouldReturnOptionalEmptyWhenIdDoesNotExists() {
        Optional<Product> product = productRepository.findById(notExistingId);
        Assertions.assertTrue(product.isEmpty());
    }
}
