package com.scaler.myfirstspringbootproj.Controller;

import com.scaler.myfirstspringbootproj.DTO.*;
import com.scaler.myfirstspringbootproj.Service.AuthService;
import com.scaler.myfirstspringbootproj.Utils.UserMapperUtil;
import com.scaler.myfirstspringbootproj.models.User;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

@Autowired
    private AuthService authService;


    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> UsersignUp(@RequestBody SignUpRequestDto signUpRequestDto) {

        try{
            User user=authService.SignUp(signUpRequestDto.getEmail(),
                    signUpRequestDto.getPassword());
            return new ResponseEntity<>(UserMapperUtil.from(user), HttpStatus.CREATED);
        }
        catch (Exception exception){
            throw exception;
        }
    }

    @PostMapping("/login")
public ResponseEntity<LoginResponseDto> Userlogin(@RequestBody LoginRequestDto loginRequestDto){
        LoginResponseDto loginResponseDto=authService.Login(loginRequestDto.getEmail(),loginRequestDto.getPassword());

        return new ResponseEntity<>(loginResponseDto,HttpStatus.OK);
}

//ye API sirf token verify karegi
@PostMapping("/validateToken")
    public ResponseEntity<String> validateToken(@RequestBody ValidateTokenRequestDto validateTokenRequestDto){

        TokenValidationResult result=authService.validateToken(validateTokenRequestDto.getToken());
        if(!result.isValid()){
            return new ResponseEntity<>("INVALID TOKEN",HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>("VALID TOKEN",HttpStatus.OK);

}

    @PostMapping("/refresh")
    public ResponseEntity<String> refreshToken(@RequestBody RefreshTokenRequestDto refreshTokenRequestDto){

        String accessToken = authService.refreshAccessToken(refreshTokenRequestDto.getRefreshToken());

        return ResponseEntity.ok(accessToken);

    }

}
