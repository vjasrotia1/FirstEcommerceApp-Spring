package com.scaler.myfirstspringbootproj.Controller;

import com.scaler.myfirstspringbootproj.DTO.*;
import com.scaler.myfirstspringbootproj.Service.AuthService;
import com.scaler.myfirstspringbootproj.Utils.UserMapperUtil;
import com.scaler.myfirstspringbootproj.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

@Autowired
    private AuthService authService;


    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> userSignUp(@RequestBody SignUpRequestDto signUpRequestDto) {

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
public ResponseEntity<LoginResponseDto> userLogin(@RequestBody LoginRequestDto loginRequestDto){
        LoginResponseDto loginResponseDto=authService.login(loginRequestDto.getEmail(),loginRequestDto.getPassword());

MultiValueMap<String,String> headers=new LinkedMultiValueMap<>();
headers.add(HttpHeaders.SET_COOKIE,loginResponseDto.getAccessToken());

        return new ResponseEntity<>(loginResponseDto,headers,HttpStatus.OK);
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

    @PostMapping("/logout")
    //in this API, client has to send 2 things
    //1. Access Token(in header)
    //2. Refresh Token(in the requestBody)
    //because Access token in stored in UserSession table
    //and refresh token is saved in Refresh Token Table
    //and at the time of logout, we have to delete both

    //logout me 2 hi possibilities hai
    //1. success (200 OK) or exception(401 UNAUTHORISED)
    public ResponseEntity<String> UserLogOut (
            @RequestHeader("Authorization") String accessToken,
            @RequestBody LogOutRequestDto logOutRequestDto) {

        authService.logoutUser(accessToken,logOutRequestDto.getRefreshToken());

        return new ResponseEntity<>("U have Successfully Logged Out", HttpStatus.OK);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(
            @RequestBody ForgotPasswordRequestDto requestDto){

        authService.forgotPassword(requestDto.getEmail());

        return ResponseEntity.ok("Password Reset Link Sent Successfully");

    }

    @PostMapping("/google-login")
    public ResponseEntity<LoginResponseDto> googleLogin(@RequestBody GoogleLoginRequestDto dto) throws Exception {
        LoginResponseDto response = authService.loginWithGoogle(dto.getGoogleIdToken());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestBody ResetPasswordRequestDto dto){

        authService.resetPassword(
                dto.getToken(),
                dto.getNewPassword());

        return ResponseEntity.ok(
                "Password Updated");
    }

//    @GetMapping("/verify-email")
//    public ResponseEntity<String>
//    verifyEmail(@RequestParam String token){
//
//        authService.verifyEmail(token);
//
//        return ResponseEntity.ok(
//                "Email Verified");
//    }

}
