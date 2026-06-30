package com.scaler.myfirstspringbootproj.Controller;


import com.scaler.myfirstspringbootproj.DTO.CreatePaymentDto;
import com.scaler.myfirstspringbootproj.DTO.CreatePaymentRequest;
import com.scaler.myfirstspringbootproj.DTO.PaymentResponseDto;
import com.scaler.myfirstspringbootproj.Service.PaymentService;
import com.stripe.exception.StripeException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }


    @PostMapping
    public ResponseEntity<PaymentResponseDto> createPayment(
            @RequestBody CreatePaymentDto createPaymentDto) throws StripeException {

        //converting input for (creating paymentlink for order) in the form of DTO --->to DTO object for StripePaymentService
        CreatePaymentRequest createPaymentRequest=new CreatePaymentRequest(
                createPaymentDto.getOrderId(),
                createPaymentDto.getPaymentMethod()
        );
//paymentResponseDto is the response we want to give back to FE/CLIENT which comprises of OrderId and paymentURL
        PaymentResponseDto paymentResponseDto=paymentService.createPayment(createPaymentRequest);

        return new ResponseEntity<>(paymentResponseDto,HttpStatus.CREATED);
    }

}
