package com.scaler.myfirstspringbootproj.Service;

import com.scaler.myfirstspringbootproj.DTO.CreateUserRequest;
import com.scaler.myfirstspringbootproj.Repository.CartRepository;
import com.scaler.myfirstspringbootproj.Repository.UserRepository;
import com.scaler.myfirstspringbootproj.models.Cart;
import com.scaler.myfirstspringbootproj.models.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final CartRepository cartRepository;
    private UserRepository userRepository;

public UserService(UserRepository userRepository, CartRepository cartRepository) {
        this.userRepository = userRepository;
    this.cartRepository = cartRepository;
}

    public User createUser(CreateUserRequest  createUserRequest) {

        User newUser = new User(
                createUserRequest.getUserName(),
                createUserRequest.getEmail(),
                createUserRequest.getGender()
        );

        userRepository.save(newUser);
        //common ecommerce approach -- create cart when user is created

        Cart cart = new Cart();
        cart.setUser(newUser);
        cartRepository.save(cart);


        return newUser;


    }

}
