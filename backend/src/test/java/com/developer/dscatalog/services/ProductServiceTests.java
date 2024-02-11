package com.developer.dscatalog.services;

import com.developer.dscatalog.dto.ProductDTO;
import com.developer.dscatalog.entities.Category;
import com.developer.dscatalog.entities.Product;
import com.developer.dscatalog.repositories.CategoryRepository;
import com.developer.dscatalog.repositories.ProductRepository;
import com.developer.dscatalog.services.exceptions.DatabaseException;
import com.developer.dscatalog.services.exceptions.ResourceNotFoundException;
import com.developer.dscatalog.tests.Factory;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.query.sqm.EntityTypeException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService productService;
    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    private Long existingId;
    private Long nonExistingId;

    private Long dependentId;

    private Product product;

    private PageImpl<Product> page; //tipo concreto que represeta uma página

    private ProductDTO productDTO;

    private Category category;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;

        product = Factory.createProduct();
        page = new PageImpl<>(List.of(product));

        productDTO = Factory.createProductDTO();

        category = Factory.createCategory();

        //Mockito.doNothing().when(productRepository).deleteById(existingId);
        //Mockito.doThrow(EmptyResultDataAccessException.class).when(productRepository).deleteById(nonExistingId);

        Mockito.doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependentId);

        Mockito.when(productRepository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);
        Mockito.when(productRepository.save(ArgumentMatchers.any())).thenReturn(product);
        Mockito.when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
        Mockito.when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Mockito.when(productRepository.existsById(existingId)).thenReturn(true);
        Mockito.when(productRepository.existsById(nonExistingId)).thenReturn(false);
        Mockito.when(productRepository.existsById(dependentId)).thenReturn(true);

        Mockito.when(productRepository.getReferenceById(existingId)).thenReturn(product);
        Mockito.when(productRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        Mockito.when(categoryRepository.getReferenceById(existingId)).thenReturn(category);
        Mockito.when(categoryRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {

        //Mockito.when(productRepository.existsById(existingId)).thenReturn(true);

        Assertions.assertDoesNotThrow(() -> {
            productService.delete(existingId);
        });

        Mockito.verify(productRepository, Mockito.times(1)).deleteById(existingId);

    }

    @Test
    public void deleteShouldThrowResourceExceptionWhenIdDoesNotExists() {
        //Mockito.when(productRepository.existsById(nonExistingId)).thenReturn(false);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.delete(nonExistingId);
        });
    }

    @Test
    public void deleteShouldThrowDataBaseExceptionWhenIdIsDependent() {
        //Mockito.when(productRepository.existsById(dependentId)).thenReturn(true);

        Assertions.assertThrows(DatabaseException.class, () -> {
            productService.delete(dependentId);
        });
    }

    @Test
    public void findAllShouldReturnAllExistingIds() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<ProductDTO> result = productService.findAllPaged(pageable);
        Assertions.assertNotNull(result);
        Mockito.verify(productRepository, Mockito.times(1)).findAll(pageable);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenNotExistingIds() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.findById(nonExistingId);
        });
    }

    @Test
    public void findByIdShouldReturnProductDTOWhenExistingIds() {
        ProductDTO productDTO = productService.findById(existingId);
        Assertions.assertNotNull(productDTO);
    }

    @Test
    public void updateShouldReturnProductDTOWhenExistingIds() {
        ProductDTO productDTOresult = productService.update(productDTO, existingId);
        Assertions.assertNotNull(productDTOresult);
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenNonExistingIds() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.update(productDTO, nonExistingId);
        });
    }
}