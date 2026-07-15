package com.scaler.myfirstspringbootproj.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//front end will send only this
//no email, no password, no name. Google will send everything inside this token

//flow
//User clicks ---> continue with google ---> google login popUp --->User enter gmail+password-->google verifies
//and creates ID token and sends to FE--->FE calls POST/auth/google-login with this Idtoken in JSONBody
//backend never opens google login page, FE opens google pop up Page. backend sirf token verify krta hai


//"My application supports both local authentication and Google Sign-In.
// If a user initially signs up using email/password and later uses the same email with Google,
// I link the Google account by storing the Google ID while preserving the existing password.
// This allows the user to authenticate using either method without creating duplicate accounts."
public class GoogleLoginRequestDto {

    private String googleIdToken;
}
