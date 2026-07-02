package com.scaler.myfirstspringbootproj.Repository;

import com.scaler.myfirstspringbootproj.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

Optional<Payment> findByPaymentId(String paymentId);
}
