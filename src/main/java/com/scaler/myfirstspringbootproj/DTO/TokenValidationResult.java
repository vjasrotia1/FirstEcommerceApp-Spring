package com.scaler.myfirstspringbootproj.DTO;


import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

//Ye class database me save nahi hogi. isiliye @Entity use nahi karenge
//Ye sirf Controller aur Service ke beech data transfer karegi.

public class TokenValidationResult {

    private boolean valid;
    private Long userId;
    private String role;
}
