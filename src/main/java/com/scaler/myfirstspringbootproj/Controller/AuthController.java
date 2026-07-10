package com.scaler.myfirstspringbootproj.Controller;

import com.scaler.myfirstspringbootproj.DTO.LoginRequestDto;
import com.scaler.myfirstspringbootproj.DTO.SignUpRequestDto;
import com.scaler.myfirstspringbootproj.DTO.UserResponseDto;
import com.scaler.myfirstspringbootproj.DTO.ValidateTokenRequestDto;
import com.scaler.myfirstspringbootproj.Service.AuthService;
import com.scaler.myfirstspringbootproj.Utils.UserMapperUtil;
import com.scaler.myfirstspringbootproj.models.User;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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


    private AuthService authService;


    @PostMapping("/signUp")
    public ResponseEntity<UserResponseDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {

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
public ResponseEntity<UserResponseDto>  login(@RequestBody LoginRequestDto loginRequestDto){
        try{
            Pair<User,String> userTokenPair=authService.Login(loginRequestDto.getEmail(),loginRequestDto.getPassword());

            UserResponseDto userResponseDto=UserMapperUtil.from(userTokenPair.a);
            MultiValueMap<String, String> headers=new LinkedMultiValueMap<>();
            headers.add(HttpHeaders.SET_COOKIE,userTokenPair.b);
            return new ResponseEntity<>(userResponseDto,headers,HttpStatusCode.valueOf(201));
        }
        catch (Exception exception){
            throw exception;
        }
}

@PostMapping("/validateToken")
    public ResponseEntity<String>  validateToken(@RequestBody ValidateTokenRequestDto validateTokenRequestDto){

        Boolean result=authService.validateToken(validateTokenRequestDto.getToken(), validateTokenRequestDto.getUserId());
        if(result){
            return new ResponseEntity<>("SUCCESS",HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("FAILURE",HttpStatus.UNAUTHORIZED);
        }
}

}
