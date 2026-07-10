package com.scaler.myfirstspringbootproj.Repository;

import com.scaler.myfirstspringbootproj.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailEquals(String email);

    Optional<User> findByIdEquals(Long id);

    void deleteByEmailEquals(String email);
}
