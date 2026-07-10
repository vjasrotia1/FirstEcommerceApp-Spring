package com.scaler.myfirstspringbootproj.models;


import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserSession extends  BaseModel{

    @ManyToOne
    private  User user;

    private String token;

}
