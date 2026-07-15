package com.scaler.myfirstspringbootproj.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.scaler.myfirstspringbootproj.DTO.LoginResponseDto;
import com.scaler.myfirstspringbootproj.DTO.TokenValidationResult;
import com.scaler.myfirstspringbootproj.ExceptionHandling.InvalidGoogleTokenException;
import com.scaler.myfirstspringbootproj.ExceptionHandling.PasswordMismatchException;
import com.scaler.myfirstspringbootproj.ExceptionHandling.UserAlreadyExistsException;
import com.scaler.myfirstspringbootproj.ExceptionHandling.UserNotFoundException;
import com.scaler.myfirstspringbootproj.Repository.*;
import com.scaler.myfirstspringbootproj.Utils.UserMapperUtil;
import com.scaler.myfirstspringbootproj.models.*;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserSessionRepository userSessionRepository;


    @Autowired
    private SecretKey secretKey;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private PasswordResetTokenRepository  passwordResetTokenRepository;

    //for google login we have to add below 3 dependencies
    @Autowired
    private NetHttpTransport httpTransport;

    @Autowired
    private GsonFactory gsonFactory;

    @Autowired
    private EmailVerificationRepository emailVerificationRepository;

    //it means, Hey Spring, please find property named "google.client.id" in "application properties" and
    //put its value in this variable name googleClientId
    @Value("${google.client.id}")
    private String googleClientId;

    //USer signup logic
    @Transactional
    //at the time of signup, User creates LOCAL account
    public User SignUp(String email, String password){
        Optional<User> userOptional=userRepository.findByEmailEquals(email);

        if(userOptional.isPresent()){
            throw new UserAlreadyExistsException("User already exists.please login directly");
        }

        User newUser=new User();
        newUser.setEmail(email);
        newUser.setPassword(bCryptPasswordEncoder.encode(password));
        newUser.setRole(Role.ROLE_USER);
        newUser.setRegistrationProvider(LoginProvider.LOCAL);
        return userRepository.save(newUser);

    }

    //User login logic
    //both google login and normal login will use same JWT token generation. only authentication will be separate
    @Transactional
    public LoginResponseDto login(String email, String password){
        User existingUser= userRepository.findByEmailEquals(email)
                .orElseThrow(
                        () -> new UserNotFoundException("No User found with this email address, please SignUp first")
                );

        if(existingUser.getPassword()==null){
            throw new RuntimeException("This account was created using Google Login. please login with google");
        }

        String pwd= existingUser.getPassword();
        if(!bCryptPasswordEncoder.matches(password,pwd)){
            throw new PasswordMismatchException("Password provided doesn't match. Please use correct password or reset it");
        }
//ACCESS Token generation Logic,when both email and password are correct

        Map<String,Object> payLoad=new HashMap<>();

        payLoad.put("userId",existingUser.getId());
        payLoad.put("email",existingUser.getEmail());
        Long nowInMillis=System.currentTimeMillis();
        payLoad.put("iat",nowInMillis);
        //token expiry after almost 1 hour from current time as 3600*1000 millisec==1 hr)
        payLoad.put("exp",nowInMillis+3600*1000);
        payLoad.put("iss", "scaler_uas");
        payLoad.put("role",existingUser.getRole().name());

        String accessToken = Jwts.builder().claims(payLoad).signWith(secretKey).compact();

        //GENERATE REFRESH TOKEN
        String refreshToken = UUID.randomUUID().toString();
        RefreshToken rt=new RefreshToken();
        rt.setUser(existingUser);
        rt.setToken(refreshToken);
        rt.setExpiryTime(System.currentTimeMillis()+3600*1000*24*30L);
        refreshTokenRepository.save(rt);

        //SAVE USER SESSION
        //persisting above generated token in UserSession Table
        UserSession userSession=new UserSession();
        userSession.setUser(existingUser);
        userSession.setToken(accessToken);
        userSessionRepository.save(userSession);

        //RESPONSE
        return new LoginResponseDto(
                UserMapperUtil.from(existingUser),
                accessToken,
                refreshToken
        );
    }

    //server will validate token string which F/E or browser is sending
public TokenValidationResult validateToken(String token){

        Optional<UserSession> OptionaluserSession=
                userSessionRepository.findByToken(token);

        if(OptionaluserSession.isEmpty()){
            return new TokenValidationResult(false,null,null);
        }

        String persistedToken=OptionaluserSession.get().getToken();
        if(!persistedToken.equals(token)){
            return new TokenValidationResult(false,null,null);
        }

        //Parsing this persistedToken to get Payload to get expiry
    //here the server is saying that token is present in DB, but we need to
    //check if any hacker has modified the token
    //so we need to verify the token signature with the help of secretkey
    //ideally token passed in input param and persistedToken are same
    //but to prevent certain types of SQL collation bypasses, we can use it(persisted token stored in our secure database) for an exact string comparison
    //Use it to parse instead of the user/FE/Browser/client -supplied string
    try {
        JwtParser parserPayLoad =
                Jwts.parser()
                        .verifyWith(secretKey)
                        .build();

        //The jjwt library automatically validates the expiry claim during .parseSignedClaims().
        // If the token is expired, it throws an ExpiredJwtException.
        // You do not need to manually extract and check expiry
        //verify signature and expiration automatically
        Claims claims= parserPayLoad.
                parseSignedClaims(persistedToken)
                //parseSignedClaims internally verify karti hai : 1. signature,Expiry,Tampered Token,Invalid token, Malformed Token
                //isiliye manually expiry check krne ki need nahi hai
                .getPayload();

        return new TokenValidationResult(
                true,
                claims.get("userId",Long.class),
                claims.get("role",String.class)
        );
    }catch(JwtException | IllegalArgumentException e) {
        // Catches expired, tampered, malformed, or empty tokens safely
        return new TokenValidationResult(false,null,null);
    }
}
//    public boolean isAdmin(Long userId){
//
//        User user =
//                userRepository.findByIdEquals(userId)
//                        .orElseThrow();
//
//        return user.getRole()==Role.ROLE_ADMIN;
//    }

    @Transactional
    public String refreshAccessToken(String refreshToken){
        RefreshToken token =
                refreshTokenRepository
                        .findByToken(refreshToken)
                        .orElseThrow(
                                ()->new RuntimeException("Invalid Refresh Token")
                        );

        if(token.getExpiryTime()
                < System.currentTimeMillis()){

            throw new RuntimeException(
                    "Refresh Token Expired");
        }
// Get User associated with Refresh Token
        User user = token.getUser();

        //get JWT payload
    Map<String,Object> payLoad=new HashMap<>();
        payLoad.put("userId",user.getId());
        payLoad.put("role",user.getRole().name());
        payLoad.put("email",user.getEmail());
        payLoad.put("iss", "scaler_uas");
        payLoad.put("iat",System.currentTimeMillis());
        payLoad.put("exp",System.currentTimeMillis()+3600*1000); //1 hour validity

//now generate new Access Token
        String newAccessToken = Jwts.builder().claims(payLoad).signWith(secretKey).compact();

        //persist this new Access Token
        UserSession userSession = new UserSession();
        userSession.setUser(user);
        userSession.setToken(newAccessToken);
        userSessionRepository.save(userSession);

        return newAccessToken;
    }

@Transactional
    public void logoutUser(String accessToken, String refreshToken){

        if(accessToken==null || !accessToken.startsWith("Bearer ")){
            throw new RuntimeException(
                    "Authorization header missing");
        }
// Remove "Bearer " from Authorization header
        if(accessToken.startsWith("Bearer ")){
            accessToken = accessToken.substring(7);
        }

//validate or verify access token
        TokenValidationResult tokenValidationResult = validateToken(accessToken);

        if(!tokenValidationResult.isValid()){
            throw new RuntimeException("Invalid Access Token");
        }

        //check refresh Token

        RefreshToken refreshTokenEntity = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(
                        ()->new RuntimeException("Invalid Refresh Token")
                );

    if(refreshTokenEntity.getExpiryTime()
            < System.currentTimeMillis()){

        refreshTokenRepository.delete(
                refreshTokenEntity);

        throw new RuntimeException(
                "Refresh Token Expired");
    }

        // Optional Security Check
        if (!refreshTokenEntity.getUser().getId().equals(tokenValidationResult.getUserId())) {
            throw new RuntimeException(
                    "Access Token and Refresh Token belong to different users");
        }

        //delete access Token
        userSessionRepository.deleteByToken(accessToken);

        //delete refresh Token
        refreshTokenRepository.deleteByToken(refreshToken);


        //production level improvement
        //if user is logged in via Mobile,Laptop,ipad and logs out on Mobile
        //so only access token and refresh token of Mobile should get deleted and not of Laptop and ipad
        //Isliye production me deleteByToken() bahut achha design hai, kyunki ye specific session ko logout karta hai.
        //Baad me agar tum "Logout from All Devices" implement karna chaho, to naya repository method bana sakte ho:
        //e.g. void deleteByUser(user user) or void deleteAllByUser_Id(Long userId)
        //Isse ek hi query me us user ke saare access tokens aur saare refresh tokens delete ho jayenge.
        // Ye feature Google, Facebook aur banking apps me bhi milta hai ("Sign out of all devices").
    }

    public void forgotPassword(String email) {

        // Check whether user exists
        User user = userRepository.findByEmailEquals(email)
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        passwordResetTokenRepository
                .findByUserAndUsedFalse(user)
                .ifPresent(existing -> {
                    existing.setUsed(true);
                    passwordResetTokenRepository.save(existing);
                });

        // Generate Reset Token
        String token = UUID.randomUUID().toString();

        // Create PasswordResetToken object
        PasswordResetToken passwordResetToken =
                new PasswordResetToken();

        passwordResetToken.setUser(user);
        passwordResetToken.setToken(token);

        // Valid for 1 hour
        passwordResetToken.setExpiryTime(
                System.currentTimeMillis() + (60 * 60 * 1000)
        );

        passwordResetToken.setUsed(false);

        // Save in database
        passwordResetTokenRepository.save(passwordResetToken);

        // TODO
        // Send Email
    }

    public LoginResponseDto loginWithGoogle(String idTokenString) {
//step 1- verify google token
        GoogleIdToken.Payload payload =
                verifyGoogleToken(idTokenString);
//step 2- extract details from payLoad
        String googleId = payload.getSubject();
        String email = payload.getEmail();
        String name = (String) payload.get("name");

        Boolean emailVerified = payload.getEmailVerified();

        if(Boolean.FALSE.equals(emailVerified)){
            throw new RuntimeException(
                    "Google Email is not verified");
        }

        User user;

        //case 1- GOOGLE ACCOUNT ALREADY LINKED

        Optional<User> googleUser =
                userRepository.findByGoogleId(googleId);

        if (googleUser.isPresent()) {

            user = googleUser.get();
        }

        //case 2 - GoogleId not found
            else{
                Optional<User> emailUser =
                        userRepository.findByEmailEquals(email);

                //case 2-A : Local Account exists

            if (emailUser.isPresent()) {

                user = emailUser.get();

                // Link Google Account
                user.setGoogleId(googleId);

                userRepository.save(user);
            }

            //case 2-B : Completely New User

            else{

                user = new User();

                user.setUsername(name);

                user.setEmail(email);

                user.setGoogleId(googleId);

                user.setPassword(null);

                user.setRole(Role.ROLE_USER);

                user.setRegistrationProvider(
                        LoginProvider.GOOGLE
                );

                user = userRepository.save(user);
            }

        }
        //Generate Access Token

        Map<String, Object> claims = new HashMap<>();

        claims.put("userId", user.getId());

        claims.put("email", user.getEmail());

        claims.put("role", user.getRole().name());

        claims.put("iat", System.currentTimeMillis());

        claims.put("exp",
                System.currentTimeMillis() + 60 * 60 * 1000);

        claims.put("iss", "scaler_uas");


        String accessToken =
                Jwts.builder()
                        .claims(claims)
                        .signWith(secretKey)
                        .compact();

        //GENERATE REFRESH TOKEN

        String refreshToken =
                UUID.randomUUID().toString();

        RefreshToken rt =
                new RefreshToken();

        rt.setUser(user);

        rt.setToken(refreshToken);

        rt.setExpiryTime(
                System.currentTimeMillis()
                        + 30L * 24 * 60 * 60 * 1000
        );

        refreshTokenRepository.save(rt);

        //save User Session
        UserSession session = new UserSession();

                session.setUser(user);

                session.setToken(accessToken);

                userSessionRepository.save(session);

                //RESPONSE
        LoginResponseDto response = new LoginResponseDto();

        response.setUser(UserMapperUtil.from(user));

        response.setAccessToken(accessToken);

        response.setRefreshToken(refreshToken);

        return response;

    }

    //Is method me JWT generate nahi hoga, database access nahi hoga, UserSession save nahi hogi.
    //Ye sirf Google se aaya hua token verify karega.


    @Transactional
    public void resetPassword(String token,
                              String newPassword){

        PasswordResetToken resetToken =
                passwordResetTokenRepository
                        .findByToken(token)
                        .orElseThrow(
                                ()->new RuntimeException(
                                        "Invalid Token")
                        );

        if(resetToken.getExpiryTime()
                <System.currentTimeMillis()){

            passwordResetTokenRepository
                    .delete(resetToken);

            throw new RuntimeException(
                    "Reset Token Expired");
        }

        User user =
                resetToken.getUser();

        user.setPassword(
                bCryptPasswordEncoder.encode(
                        newPassword)
        );

        userRepository.save(user);

        passwordResetTokenRepository
                .delete(resetToken);
    }

//    @Transactional -- TO DO
//    public void verifyEmail(String token){
//
//        EmailVerificationToken entity =
//                emailVerificationRepository
//                        .findByToken(token)
//                        .orElseThrow();
//
//        User user =
//                entity.getUser();
//
//        user.setEmailVerified(true);
//
//        userRepository.save(user);
//
//        emailVerificationRepository.delete(entity);
//    }


    private GoogleIdToken.Payload verifyGoogleToken(String idTokenString){

        GoogleIdTokenVerifier verifier =
                new GoogleIdTokenVerifier.Builder(
                        httpTransport,
                        gsonFactory
                )
                        //Google Client ID uniquely identifies my application to Google's OAuth service.
                        //During backend token verification, the GoogleIdTokenVerifier checks that the aud
                        // (audience) claim inside the received ID token matches my application's Client ID.
                        // This ensures that the token was actually issued for my application and prevents
                        // tokens generated for another application from being accepted
                        .setAudience(Collections.singletonList(googleClientId))
                        .build();

        GoogleIdToken googleIdToken;

        try {

            googleIdToken = verifier.verify(idTokenString);

        } catch (GeneralSecurityException | IOException e) {

            throw new RuntimeException("Unable to verify Google Token", e);
        }

        if (googleIdToken == null) {
            throw new InvalidGoogleTokenException("Invalid Google Token");
        }

        return googleIdToken.getPayload();
    }
}
