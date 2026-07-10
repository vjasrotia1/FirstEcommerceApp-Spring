package com.scaler.myfirstspringbootproj.Repository;

import com.scaler.myfirstspringbootproj.models.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserSessionRepository extends JpaRepository<UserSession,Long> {

    Optional<UserSession> findByTokenAndUser_Id(String token, Long userId);
}
