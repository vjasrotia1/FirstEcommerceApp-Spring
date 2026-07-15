package com.scaler.myfirstspringbootproj.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class EmailVerificationToken extends BaseModel {

    @OneToOne
    private User user;

    @Column(unique = true)
    private String token;

    private Long expiryTime;
}
