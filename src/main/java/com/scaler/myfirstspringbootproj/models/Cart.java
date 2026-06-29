package com.scaler.myfirstspringbootproj.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Entity
@Getter
@Service
@AllArgsConstructor
@NoArgsConstructor
public class Cart extends BaseModel{

    @OneToOne
    private  User user;

    @ManyToMany
    private List<Product> products;

    @Override
    public String toString() {
        return "Cart{" +
                "user=" + user +
                ", products=" + products +
                '}';
    }
}
