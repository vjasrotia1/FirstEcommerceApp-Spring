package com.scaler.myfirstspringbootproj.Controller;

import com.scaler.myfirstspringbootproj.DTO.CreateNewCategoryRequest;
import com.scaler.myfirstspringbootproj.DTO.NewCategoryDto;
import com.scaler.myfirstspringbootproj.Service.CategoryService;
import com.scaler.myfirstspringbootproj.models.Category;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private CategoryService categoryService;

    public CategoryController(CategoryService categoryService)
    {
        this.categoryService=categoryService;
    }

    //get single category by ID
    @GetMapping("/{id}")
    public ResponseEntity<Category> getsingleCategory(Long id){

        Category c= categoryService.getSingleCategory(id);
        return new ResponseEntity<>(c, HttpStatus.OK);
    }

    //get all categories
    @GetMapping
    public ResponseEntity<List<Category>> getallcategories() {
    List<Category> categories=categoryService.getAllCategories();

        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    //create category
@PostMapping
        public ResponseEntity<Category> createCategory(@RequestBody NewCategoryDto newCategorydto) {

            CreateNewCategoryRequest  createNewCategoryRequest=new CreateNewCategoryRequest(
                    newCategorydto.getCategoryName()
            );

            Category newCategory= categoryService.createCategory(createNewCategoryRequest);

            return new ResponseEntity<>(newCategory, HttpStatus.CREATED);

        }

        //delete category

}

