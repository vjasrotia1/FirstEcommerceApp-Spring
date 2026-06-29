package com.scaler.myfirstspringbootproj.Service;

import com.stripe.exception.StripeException;

public interface PaymentService {

    String makePayment(Long orderId, Long orderAmount) throws StripeException;
}
