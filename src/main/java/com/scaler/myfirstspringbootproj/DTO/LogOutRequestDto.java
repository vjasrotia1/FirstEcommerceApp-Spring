package com.scaler.myfirstspringbootproj.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogOutRequestDto {

    private String refreshToken;
    //notice that access token is not here, because it will come via Header
}
