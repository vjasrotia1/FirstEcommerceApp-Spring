package com.scaler.myfirstspringbootproj.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
//this table is only used for resetting password
//if user successfully resets password (means used==true) - then we can delete that particular record
public class PasswordResetToken extends BaseModel {
    @ManyToOne //cardinality is ManyToOne as same User can click on forgot Password multiple Times
    private User user;

    @Column(unique = true)
    private String token;

    private Long expiryTime;

    //to keep record that whether this password is already used or not
    private boolean used;
}
