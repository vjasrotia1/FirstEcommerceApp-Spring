package com.scaler.myfirstspringbootproj.Controller;

import com.scaler.myfirstspringbootproj.DTO.CreateUserRequest;
import com.scaler.myfirstspringbootproj.DTO.UserDto;
import com.scaler.myfirstspringbootproj.Service.UserService;
import com.scaler.myfirstspringbootproj.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDto userDto) {

        CreateUserRequest createUserRequest = new CreateUserRequest(
                userDto.getUserName(),
                userDto.getEmail(),
                userDto.getGender()
        );

        User user = userService.createUser(createUserRequest);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
