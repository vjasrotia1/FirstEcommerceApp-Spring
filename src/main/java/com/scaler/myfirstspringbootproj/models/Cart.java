package com.scaler.myfirstspringbootproj.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Cart extends BaseModel{

    @OneToOne
    private  User user;

    @OneToMany(mappedBy = "cart",
    cascade = CascadeType.ALL)
    private List<CartItem> cartItems = new ArrayList<>();


}
