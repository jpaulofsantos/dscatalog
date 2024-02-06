package com.developer.dscatalog.services;

import com.developer.dscatalog.repositories.ProductRepository;
import com.developer.dscatalog.services.exceptions.DatabaseException;
import com.developer.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService productService;
    @Mock
    private ProductRepository productRepository;

    private Long existingId;
    private Long nonExistingId;

    private Long dependentId;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;
        //Mockito.doNothing().when(productRepository).deleteById(existingId);
        //Mockito.doThrow(EmptyResultDataAccessException.class).when(productRepository).deleteById(nonExistingId);
        Mockito.doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependentId);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExists() {

        Mockito.when(productRepository.existsById(existingId)).thenReturn(true);

        Assertions.assertDoesNotThrow(() -> {
            productService.delete(existingId);
        });

        Mockito.verify(productRepository, Mockito.times(1)).deleteById(existingId);

    }

    @Test
    public void deleteShouldThrowResourceExceptionWhenIdDoesNotExists() {
        Mockito.when(productRepository.existsById(nonExistingId)).thenReturn(false);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.delete(nonExistingId);
        });
    }

    @Test
    public void deleteShouldThrowDataBaseExceptionWhenIdIsDependent() {
        Mockito.when(productRepository.existsById(dependentId)).thenReturn(true);

        Assertions.assertThrows(DatabaseException.class, () -> {
            productService.delete(dependentId);
        });
    }
}
