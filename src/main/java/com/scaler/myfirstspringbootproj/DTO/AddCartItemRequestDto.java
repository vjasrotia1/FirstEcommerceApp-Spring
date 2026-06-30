package com.scaler.myfirstspringbootproj.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddCartItemRequestDto {

    private Long productId;
    private Integer quantity;
}
