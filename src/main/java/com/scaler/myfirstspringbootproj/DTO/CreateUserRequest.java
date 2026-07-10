package com.scaler.myfirstspringbootproj.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {

    private String userName;
    private String email;
    private String gender;
    private String password;

}
