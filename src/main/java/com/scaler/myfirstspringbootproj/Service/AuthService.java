package com.scaler.myfirstspringbootproj.Service;

import com.scaler.myfirstspringbootproj.ExceptionHandling.PasswordMismatchException;
import com.scaler.myfirstspringbootproj.ExceptionHandling.UserAlreadyExistsException;
import com.scaler.myfirstspringbootproj.ExceptionHandling.UserNotFoundException;
import com.scaler.myfirstspringbootproj.Repository.UserRepository;
import com.scaler.myfirstspringbootproj.Repository.UserSessionRepository;
import com.scaler.myfirstspringbootproj.models.User;
import com.scaler.myfirstspringbootproj.models.UserSession;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.security.MacAlgorithm;

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

    //USer signup logic
    public User SignUp(String email, String password){
        Optional<User> userOptional=userRepository.findByEmailEquals(email);

        if(userOptional.isPresent()){
            throw new UserAlreadyExistsException("User already exists.please login directly");
        }

        User newUser=new User();
        newUser.setEmail(email);
        newUser.setPassword(bCryptPasswordEncoder.encode(password));
        return userRepository.save(newUser);

    }

    //User login logic
    public Pair<User,String> Login(String email, String password){
        User existingUser= userRepository.findByEmailEquals(email)
                .orElseThrow(
                        () -> new UserNotFoundException("please SignUp first")
                );

        String pwd= existingUser.getPassword();
        if(!bCryptPasswordEncoder.matches(password,pwd)){
            throw new PasswordMismatchException("Passwords don't match. Please use correct password or reset it");
        }
//Token generation Logic
        Map<String,Object> claims=new HashMap<>();

        claims.put("userId",existingUser.getId());
        Long nowInMillis=System.currentTimeMillis();
        claims.put("iat",nowInMillis);
        claims.put("exp",nowInMillis+3600*1000);
        claims.put("iss", "scaler_uas");

        String token = Jwts.builder().claims(claims).signWith(secretKey).compact();

        //persisting above generated token
        UserSession userSession=new UserSession();
        userSession.setId(existingUser.getId());
        userSession.setToken(token);
        userSessionRepository.save(userSession);

        return new Pair<User,String>(existingUser,token);
    }

public  boolean validateToken(String token,Long userId){
        Optional<UserSession> OptionaluserSession=userSessionRepository.findByTokenAndUser_Id(token,userId);

        if(OptionaluserSession.isEmpty()){
            return false;
        }

        UserSession userSession=OptionaluserSession.get();

        String persistedToken=userSession.getToken();

        //Parsing this persistedToken to get Payload to get expiry
     JwtParser jwtParser=Jwts.parser().verifyWith(secretKey).build();
     Claims claims=jwtParser.parseSignedClaims(token).getPayload();

     Long expiry=(Long)claims.get("exp");

     Long currentTime=System.currentTimeMillis();

     if(currentTime>expiry){
         return false;
     }
return true;
}


}
