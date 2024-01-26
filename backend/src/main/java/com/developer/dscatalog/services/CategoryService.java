package com.developer.dscatalog.services;

import com.developer.dscatalog.entities.Category;
import com.developer.dscatalog.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService  {

    @Autowired
    CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<Category> findAll(){
        return categoryRepository.findAll();
    }
}
