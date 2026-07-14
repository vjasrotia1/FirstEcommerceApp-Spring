package com.scaler.myfirstspringbootproj.configs;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import javax.crypto.SecretKey;


@Configuration
//this is Spring Boot configuration file that sets up security tools
// and temporarily disables default security checks so you can test your APIs freely.
public class AuthConfiguration {

    @Bean
    //BCryptPasswordEncoder does the password hashing
    public BCryptPasswordEncoder getBCryptPasswordEncoder() {
        return  new BCryptPasswordEncoder();
    }

    //now here i need to define some object with which i will disable Spring Security
    //this over-rides spring security's strict default rules
    //csrf.disable() & cors.disable(): Turns off cross-site request protection features.
    //This prevents Postman tests or frontend from being blocked by security errors during local development.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                //this tells springboot to let every single request pass thru to your controllers without needing login,API key or token
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());
        return httpSecurity.build();
    }

    @Bean
    //this automatically generates a highly secure,randomised cryptographic key using HS256 algo
    //we will use this secretKey inside ur authservice to digitally sign your JWT tokens.
    //this ensures hackers can't alter or forge ur login tokens
    public SecretKey getSecretKey() {
        MacAlgorithm algorithm= Jwts.SIG.HS256;
        SecretKey secretKey= algorithm.key().build();
        return secretKey;
    }
}
