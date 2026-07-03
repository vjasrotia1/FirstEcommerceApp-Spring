package com.scaler.myfirstspringbootproj.Service;

import com.scaler.myfirstspringbootproj.DTO.CreateNewCategoryRequest;
import com.scaler.myfirstspringbootproj.ExceptionHandling.CategoryNotFoundException;
import com.scaler.myfirstspringbootproj.Repository.CategoryRepository;
import com.scaler.myfirstspringbootproj.models.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SelfCategoryService implements categoryService {

    private CategoryRepository categoryRepository;

    public SelfCategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    //get single category

    public Category getSingleCategory(Long id) throws CategoryNotFoundException {
        Category finalcategory= categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("category not found in our DB"));
        return finalcategory;

    }
    //get all categories

    public List<Category> getAllCategories() {
        List<Category> categories= categoryRepository.findAll();
        return categories;
    }



    //create a new category

    public Category createNewCategory(CreateNewCategoryRequest  createNewCategoryRequest){

        Category newCategory=new Category();

        newCategory.setName(createNewCategoryRequest.getCategoryName());
        return categoryRepository.save(newCategory);
    }

    //update a category

    //patch a category

    //delete a category

}
