package com.scaler.myfirstspringbootproj.Service;

import com.scaler.myfirstspringbootproj.DTO.CreateNewCategoryRequest;
import com.scaler.myfirstspringbootproj.ExceptionHandling.CategoryNotFoundException;
import com.scaler.myfirstspringbootproj.Repository.CategoryRepo;
import com.scaler.myfirstspringbootproj.models.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private CategoryRepo categoryRepo;

    public CategoryService(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    //get single category

    public Category getSingleCategory(Long id) throws CategoryNotFoundException {
        Category finalcategory=categoryRepo.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("category not found in our DB"));
        return finalcategory;

    }
    //get all categories

    public List<Category> getAllCategories() {
        List<Category> categories=categoryRepo.findAll();
        return categories;
    }

    //create a new category

    public Category createCategory(CreateNewCategoryRequest  createNewCategoryRequest){

        Category newCategory=new Category();

        newCategory.setName(createNewCategoryRequest.getCategoryName());
        return categoryRepo.save(newCategory);
    }

    //update a category

    //patch a category

    //delete a category

}
