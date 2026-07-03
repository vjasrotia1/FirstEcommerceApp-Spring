package com.scaler.myfirstspringbootproj.Controller;


import com.scaler.myfirstspringbootproj.Service.fakestoreCategoryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fkstoreapi/categories")
public class FakeStoreCategoryController {

    private fakestoreCategoryService fakestoreCategoryService;

    public FakeStoreCategoryController(fakestoreCategoryService fakestoreCategoryService) {
        this.fakestoreCategoryService = fakestoreCategoryService;
    }

}
