package com.scaler.myfirstspringbootproj.Service;

import com.scaler.myfirstspringbootproj.DTO.CreateNewCategoryRequest;
import com.scaler.myfirstspringbootproj.models.Category;

import java.util.List;

public interface categoryService {

    //get single

    Category getSingleCategory(Long id);


    //getall
    List<Category> getAllCategories();

    //create
    Category createNewCategory(CreateNewCategoryRequest createNewCategoryRequest);

    //update category - put

    //upate category- PATCH

    //delete
}
