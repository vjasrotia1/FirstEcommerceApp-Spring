package com.scaler.myfirstspringbootproj.Controller;



import com.scaler.myfirstspringbootproj.Service.PaymentWebHookService;

import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import com.stripe.model.Event;
import org.springframework.web.bind.annotation.*;


//ideally in real world scenario
//the controller should only :
//1.receive the webhook
//2.verify the signature
//3.pass the event to a service (webhook service) which should have all the business logic
@RestController
@RequestMapping("/payments")
public class PaymentWebhookController {
    private PaymentWebHookService paymentWebHookService;


    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    public PaymentWebhookController(
            PaymentWebHookService paymentWebHookService
    ){
        this.paymentWebHookService = paymentWebHookService;
    }



    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(

            @RequestBody String payload,

            @RequestHeader("Stripe-Signature") String sigHeader

    ){
        Event event;
        try {
            event = Webhook.constructEvent(
                    payload,
                    sigHeader,
                    webhookSecret

            );
        }
        catch(Exception e){

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
        if("checkout.session.completed"
                        .equals(event.getType())
        ){
            paymentWebHookService.handlePaymentSuccess(event);
        }


        return ResponseEntity.ok("received");
    }
}


