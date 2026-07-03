package com.scaler.myfirstspringbootproj.Controller;

import com.scaler.myfirstspringbootproj.DTO.CreateNewCategoryRequest;
import com.scaler.myfirstspringbootproj.DTO.ErrorDto;
import com.scaler.myfirstspringbootproj.DTO.NewCategoryDto;
import com.scaler.myfirstspringbootproj.ExceptionHandling.CategoryNotFoundException;
import com.scaler.myfirstspringbootproj.Service.SelfCategoryService;
import com.scaler.myfirstspringbootproj.models.Category;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class SelfCategoryController {

    private SelfCategoryService SelfCategoryService;

    public SelfCategoryController(SelfCategoryService SelfCategoryService)
    {
        this.SelfCategoryService = SelfCategoryService;
    }

    //get single category by ID
    @GetMapping("/{id}")
    public ResponseEntity<Category> getsingleCategory(Long id){

        Category c= SelfCategoryService.getSingleCategory(id);
        return new ResponseEntity<>(c, HttpStatus.OK);
    }

    //get all categories
    @GetMapping
    public ResponseEntity<List<Category>> getallcategories() {
    List<Category> categories= SelfCategoryService.getAllCategories();

        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    //create category
@PostMapping
        public ResponseEntity<Category> createCategory(@RequestBody NewCategoryDto newCategorydto) {

            CreateNewCategoryRequest  createNewCategoryRequest=new CreateNewCategoryRequest(
                    newCategorydto.getCategoryName()
            );

            Category newCategory= SelfCategoryService.createNewCategory(createNewCategoryRequest);

            return new ResponseEntity<>(newCategory, HttpStatus.CREATED);

        }

        //delete category


    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorDto> handleCategoryNotFoundException(Exception e){
        ErrorDto errorDTO;
        errorDTO = new ErrorDto();
        errorDTO.setMessage(e.getMessage());

        ResponseEntity<ErrorDto> res;
        res = new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);

        return res;
    }

}

