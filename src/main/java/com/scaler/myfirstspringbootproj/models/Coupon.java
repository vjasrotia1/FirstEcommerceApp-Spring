package com.scaler.myfirstspringbootproj.models;


import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Coupon extends BaseModel{
private String code;
private Double discount;
private LocalDate expiryDate;
private Boolean active;

    @Override
    public String toString() {
        return "Coupon{" +
                "code='" + code + '\'' +
                ", discount=" + discount +
                ", expiryDate=" + expiryDate +
                ", active=" + active +
                '}';
    }
}
