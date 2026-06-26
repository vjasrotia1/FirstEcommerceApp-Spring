package com.scaler.myfirstspringbootproj.DTO;


import lombok.Getter;
import lombok.Setter;

//we have introduced a strict validation DTO for creating a new Product to restrict the user/FE/client to inject only
//those params which we intend to accept
@Getter
@Setter
public class CreateProductRequestDto {

    private String title;

    private Double price;

    private String description;

    private String categoryName;

    private String imageUrl;
}
