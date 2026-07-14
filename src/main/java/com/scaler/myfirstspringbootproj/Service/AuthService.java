package com.scaler.myfirstspringbootproj.Service;

import com.scaler.myfirstspringbootproj.DTO.LoginResponseDto;
import com.scaler.myfirstspringbootproj.DTO.TokenValidationResult;
import com.scaler.myfirstspringbootproj.ExceptionHandling.PasswordMismatchException;
import com.scaler.myfirstspringbootproj.ExceptionHandling.UserAlreadyExistsException;
import com.scaler.myfirstspringbootproj.ExceptionHandling.UserNotFoundException;
import com.scaler.myfirstspringbootproj.Repository.RefreshTokenRepository;
import com.scaler.myfirstspringbootproj.Repository.UserRepository;
import com.scaler.myfirstspringbootproj.Repository.UserSessionRepository;
import com.scaler.myfirstspringbootproj.Utils.UserMapperUtil;
import com.scaler.myfirstspringbootproj.models.RefreshToken;
import com.scaler.myfirstspringbootproj.models.Role;
import com.scaler.myfirstspringbootproj.models.User;
import com.scaler.myfirstspringbootproj.models.UserSession;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.security.MacAlgorithm;
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

    //USer signup logic
    @Transactional
    public User SignUp(String email, String password){
        Optional<User> userOptional=userRepository.findByEmailEquals(email);

        if(userOptional.isPresent()){
            throw new UserAlreadyExistsException("User already exists.please login directly");
        }

        User newUser=new User();
        newUser.setEmail(email);
        newUser.setPassword(bCryptPasswordEncoder.encode(password));
        newUser.setRole(Role.ROLE_USER);
        return userRepository.save(newUser);

    }

    //User login logic
    @Transactional
    public LoginResponseDto Login(String email, String password){
        User existingUser= userRepository.findByEmailEquals(email)
                .orElseThrow(
                        () -> new UserNotFoundException("No User found with this email address, please SignUp first")
                );

        String pwd= existingUser.getPassword();
        if(!bCryptPasswordEncoder.matches(password,pwd)){
            throw new PasswordMismatchException("Password provided doesn't match. Please use correct password or reset it");
        }
//Token generation Logic,when both email and password are correct

        Map<String,Object> payLoad=new HashMap<>();

        payLoad.put("userId",existingUser.getId());
        Long nowInMillis=System.currentTimeMillis();
        payLoad.put("iat",nowInMillis);
        //token expiry after almost 1 hour from current time as 3600*1000 millisec==1 hr)
        payLoad.put("exp",nowInMillis+3600*1000);
        payLoad.put("iss", "scaler_uas");
        payLoad.put("role",existingUser.getRole());

        String accessToken = Jwts.builder().claims(payLoad).signWith(secretKey).compact();
        String refreshToken = UUID.randomUUID().toString();


        RefreshToken rt=new RefreshToken();
        rt.setUser(existingUser);
        rt.setToken(refreshToken);
        rt.setExpiryTime(System.currentTimeMillis()+3600*1000*24*30L);

        refreshTokenRepository.save(rt);

        //persisting above generated token in UserSession Table
        UserSession userSession=new UserSession();
        userSession.setUser(existingUser);
        userSession.setToken(accessToken);
        userSessionRepository.save(userSession);

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

        payLoad.put("iss", "scaler_uas");
        payLoad.put("iat",System.currentTimeMillis());
        payLoad.put("exp",System.currentTimeMillis()+3600*1000); //1 hour validity

//now generate new Access Token
        String newAccessToken = Jwts.builder().claims(payLoad).signWith(secretKey).compact();

        //persist new Access Token
        UserSession userSession = new UserSession();
        userSession.setUser(user);
        userSession.setToken(newAccessToken);

        userSessionRepository.save(userSession);


        return newAccessToken;
    }

}
