package com.scaler.myfirstspringbootproj.DTO;


import com.scaler.myfirstspringbootproj.models.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//this is kind of DTO for AuthService which is copying UserDto sent from FE/client
//UserDto should not be directly send to AuthService to ensure loose coupling
//UserDto from FE/client should only be uptill Controller
public class CreateUserRequest {

    private String userName;
    private String email;
    private String gender;
    private String password;
    private String role;
    private String registrationProvider;
    private String  googleId;

}
