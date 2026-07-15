package com.scaler.myfirstspringbootproj.Repository;

import com.scaler.myfirstspringbootproj.models.User;
import com.scaler.myfirstspringbootproj.models.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession,Long> {

    //underscore User_Id represents nested Property Navigation
    //basically it means
    //UserSession class me token aur user naam k fields hai aur User class k andar id naam ka ek field hai
    //spring JPA isko user.id ki tarah samjhta hai
    Optional<UserSession> findByToken(String token);

    void deleteByToken(String token);
    void deleteByUser(User user);
}
