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
//this class is introduced to maintain active sessions
//after logout, corresponding user_session row will be deleted and persisted token will
//also be deleted
public class UserSession extends  BaseModel{

    @ManyToOne
    private  User user;

    private String token;
//to maintain session history
    private State userSessionState;

}
