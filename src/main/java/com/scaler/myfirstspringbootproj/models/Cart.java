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

    //mappedBy means the datatype of field "cart" in CartItem class is mapped as OneToMany with CartItem class
    @OneToMany(mappedBy = "cart",
    cascade = CascadeType.ALL)
    private List<CartItem> cartItems = new ArrayList<>();


}
