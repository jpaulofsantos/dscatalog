package com.developer.dscatalog.services;

import com.developer.dscatalog.dto.CategoryDTO;
import com.developer.dscatalog.entities.Category;
import com.developer.dscatalog.repositories.CategoryRepository;
import com.developer.dscatalog.services.exceptions.DatabaseException;
import com.developer.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService  {

    @Autowired
    CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAllPaged(Pageable pageable){
        Page<Category> list = categoryRepository.findAll(pageable);
        return list.map(x-> new CategoryDTO(x));
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id){
        Optional<Category> category = categoryRepository.findById(id);
        Category cat = category.orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
        return new CategoryDTO(cat);
    }

    @Transactional
    public CategoryDTO insert(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        category = categoryRepository.save(category);

        return new CategoryDTO(category);
    }

    @Transactional
    public CategoryDTO update(Long id, CategoryDTO categoryDTO) {
        try {
            Category category = categoryRepository.getReferenceById(id);
            category.setName(categoryDTO.getName());
            category = categoryRepository.save(category);
            return new CategoryDTO(category);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id " + id + " not found");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Id " + id + " not found");
        }
        try {
            categoryRepository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial");
        }
    }
}
