package com.scaler.myfirstspringbootproj.Controller;

import com.scaler.myfirstspringbootproj.DTO.CreateUserRequest;
import com.scaler.myfirstspringbootproj.DTO.ErrorDto;
import com.scaler.myfirstspringbootproj.DTO.UserDto;
import com.scaler.myfirstspringbootproj.DTO.UserResponseDto;
import com.scaler.myfirstspringbootproj.ExceptionHandling.UserNotFoundException;
import com.scaler.myfirstspringbootproj.Service.UserService;
import com.scaler.myfirstspringbootproj.Utils.UserMapperUtil;
import com.scaler.myfirstspringbootproj.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                userDto.getGender(),
                userDto.getPassword()
        );

        User user = userService.createUser(createUserRequest);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDto> handleUserNotFoundException(Exception e){
        ErrorDto errorDTO;
        errorDTO = new ErrorDto();
        errorDTO.setMessage(e.getMessage());

        return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public UserResponseDto getUserById(@PathVariable  Long id) {
        User user = userService.getUserById(id);
        if(user == null) {
            throw new RuntimeException("User with Id not found");
        }

        return UserMapperUtil.from(user);
    }

    @GetMapping("/{email}")
    public UserResponseDto getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        if(user == null) {
            throw new RuntimeException("User with Email not found");
        }

        return UserMapperUtil.from(user);
    }

    @PatchMapping("userId/{userId}/email/{newEmail}")
    public UserResponseDto updateUserEmail(@PathVariable Long userId,
                                   @PathVariable String newEmail) {

        User user = userService.updateUserEmail(userId,newEmail);
        if(user == null) {
            throw new RuntimeException("User with Id not found");
        }

        return UserMapperUtil.from(user);
    }

    @DeleteMapping("/{email}")
    public Boolean deleteUser(@PathVariable String email) {
        Boolean result = userService.deleteUser(email);
        if(!result) {
            throw new RuntimeException("User not found already");
        }else {
            return true;
        }
    }
}
