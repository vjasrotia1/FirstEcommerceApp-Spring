package com.scaler.myfirstspringbootproj.Service;

import com.scaler.myfirstspringbootproj.DTO.CreateNewCategoryRequest;
import com.scaler.myfirstspringbootproj.Repository.CategoryRepository;
import com.scaler.myfirstspringbootproj.models.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class fakestoreCategoryService implements categoryService {

    CategoryRepository categoryRepository;



    @Override
    public Category getSingleCategory(Long id) {

        return null;
    }

    @Override
    public List<Category> getAllCategories() {
        return List.of();
    }

    @Override
    public Category createNewCategory(CreateNewCategoryRequest createNewCategoryRequest) {
        return null;
    }


}
