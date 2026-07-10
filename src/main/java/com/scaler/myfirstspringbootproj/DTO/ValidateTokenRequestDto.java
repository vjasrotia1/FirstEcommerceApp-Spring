package com.scaler.myfirstspringbootproj.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidateTokenRequestDto {
    private String token;
    private Long  userId;

}
