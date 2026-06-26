package com.scaler.myfirstspringbootproj.DTO;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter

//this DTO will help in updating all fields(PUT operation) as well as selected fields(PATCH OPERATION)
public class UpdateProductRequestDto {
    private String title;
    private String description;
    private Double price;
    private  String image;
    private String category;
}
