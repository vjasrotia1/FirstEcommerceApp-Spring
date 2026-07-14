package com.scaler.myfirstspringbootproj.models;


import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken extends BaseModel{

    @ManyToOne
    //manyToOne because Laptop,Mobile,Office PC will have different RefreshTokens for same User say Varun
    private User user;
    private String token;
    private Long expiryTime;
}
