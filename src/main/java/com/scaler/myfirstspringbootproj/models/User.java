package com.scaler.myfirstspringbootproj.models;


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
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

}
