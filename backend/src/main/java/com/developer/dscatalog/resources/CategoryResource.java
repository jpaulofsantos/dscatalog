package com.developer.dscatalog.resources;

import com.developer.dscatalog.dto.CategoryDTO;
import com.developer.dscatalog.entities.Category;
import com.developer.dscatalog.services.CategoryService;
import jakarta.persistence.GeneratedValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {

    @Autowired
    CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Page<CategoryDTO>> findAllPaged(Pageable pageable) {
        Page<CategoryDTO> list = categoryService.findAllPaged(pageable);
        return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CategoryDTO> findById(@PathVariable Long id){
        CategoryDTO category = categoryService.findById(id);
        return ResponseEntity.ok(category);
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO categoryDTO) {
        categoryDTO = categoryService.insert(categoryDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(categoryDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(categoryDTO);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CategoryDTO> update(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        CategoryDTO catDTO = categoryService.update(id, categoryDTO);
        return ResponseEntity.ok(catDTO);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { //Void pois o corpo da resposta será vazio
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
