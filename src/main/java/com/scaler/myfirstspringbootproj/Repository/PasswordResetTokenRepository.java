package com.scaler.myfirstspringbootproj.Repository;

import com.scaler.myfirstspringbootproj.models.PasswordResetToken;
import com.scaler.myfirstspringbootproj.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByToken(String token);

    //if User clicks on forgot password 10 times, then in DB there will be 10 active RESET Tokens
    //in real world, we first invalidate old active reset tokens, for this we add few lines at start of forgotpassword method
    Optional<PasswordResetToken> findByUserAndUsedFalse(User user);
}
