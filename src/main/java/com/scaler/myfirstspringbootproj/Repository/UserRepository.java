package com.scaler.myfirstspringbootproj.Repository;

import com.scaler.myfirstspringbootproj.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
