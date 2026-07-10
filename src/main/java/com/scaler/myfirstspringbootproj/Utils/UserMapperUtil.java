package com.scaler.myfirstspringbootproj.Utils;

import com.scaler.myfirstspringbootproj.DTO.UserResponseDto;
import com.scaler.myfirstspringbootproj.models.User;

public class UserMapperUtil {

    public static UserResponseDto from(User user){
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setEmail(user.getEmail());
        return userResponseDto;
    }
}
