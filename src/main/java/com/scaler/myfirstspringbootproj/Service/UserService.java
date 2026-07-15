package com.scaler.myfirstspringbootproj.Service;

import com.scaler.myfirstspringbootproj.DTO.CreateUserRequest;
import com.scaler.myfirstspringbootproj.ExceptionHandling.UserNotFoundException;
import com.scaler.myfirstspringbootproj.Repository.CartRepository;
import com.scaler.myfirstspringbootproj.Repository.UserRepository;
import com.scaler.myfirstspringbootproj.models.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private CartRepository cartRepository;
    private UserRepository userRepository;

public UserService(UserRepository userRepository, CartRepository cartRepository) {
        this.userRepository = userRepository;
    this.cartRepository = cartRepository;
}

    public User createUser(CreateUserRequest  createUserRequest) {

        User newUser = new User(
                createUserRequest.getUserName(),
                createUserRequest.getEmail(),
                createUserRequest.getGender(),
                createUserRequest.getPassword(),
                Role.valueOf(createUserRequest.getRole()),
                LoginProvider.valueOf(createUserRequest.getRegistrationProvider()),
                createUserRequest.getGoogleId()
        );

        userRepository.save(newUser);
        //common ecommerce approach -- create cart when user is created

        Cart cart = new Cart();
        cart.setUser(newUser);
        cartRepository.save(cart);
        return newUser;
    }

public User getUserById(Long id){
    User user = userRepository.findByIdEquals(id)
            .orElseThrow(
                    () -> new UserNotFoundException("User not found with id " + id)
            );

    return  user;
}

public User getUserByEmail(String email){
    User user = userRepository.findByEmailEquals(email)
        .orElseThrow(()-> new UserNotFoundException("User not found with email " + email));

    return  user;
}

public User updateUserEmail(Long id, String email){
    User user = userRepository.findByIdEquals(id)
        .orElseThrow(() -> new UserNotFoundException("User not found with id " + id));

    user.setEmail(email);
    return userRepository.save(user);
}

//we are now handling soft delete and hard delete both in single API
    //Need to ask interviewer about requirement

    public Boolean deleteUser(String email){
    Optional<User> optionalUser=userRepository.findByEmailEquals(email);

    if(optionalUser.isEmpty()){
        return false;
    }
    else{
        User user=optionalUser.get();
        if(user.getState().equals(State.ACTIVE)){
            user.setState(State.INACTIVE);
            userRepository.save(user);
            return true;
        }
        else if(user.getState().equals(State.INACTIVE)){
            userRepository.deleteByEmailEquals(email);
            return true;
        }
    }
        System.out.println("something unexpected happened");
    return false;

    }

}
