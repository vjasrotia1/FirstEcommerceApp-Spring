package com.scaler.myfirstspringbootproj.DTO;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginResponseDto {
    private UserResponseDto user;
    private String accessToken;
    private String refreshToken;
}
