package com.scaler.myfirstspringbootproj.models;


import com.scaler.myfirstspringbootproj.Service.AuthService;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User extends BaseModel{
    private String username;
    private String email;
    private String gender;
    //password will be null if GOOGLE-ONLY account
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;
    //in case of google login, LoginProvider is google
    //in case of login via Email+password, LoginProvider is Local
    //Google login Users ka password is never stored in DB

    @Enumerated(EnumType.STRING)
    private LoginProvider registrationProvider;

    //googleId will be null if LOCAL-ONLY account
    private String googleId;

    //private boolean emailVerified;

}
