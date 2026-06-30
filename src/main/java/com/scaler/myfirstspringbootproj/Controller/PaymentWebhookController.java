package com.scaler.myfirstspringbootproj.Controller;


import com.scaler.myfirstspringbootproj.Repository.OrderRepository;
import com.scaler.myfirstspringbootproj.Repository.PaymentRepository;
import com.stripe.net.Webhook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/payments")
//public class PaymentWebhookController {
//
//    private PaymentRepository paymentRepository;
//
//    private OrderRepository orderRepository;
//
//
//    @Value("${stripe.webhook.secret}")
//    private String webhookSecret;
//
//
//
//    public PaymentWebhookController(
//            PaymentRepository paymentRepository,
//            OrderRepository orderRepository
//    ){
//
//        this.paymentRepository = paymentRepository;
//
//        this.orderRepository = orderRepository;
//
//    }
//
//
//
//    @PostMapping("/webhook")
//    public ResponseEntity<String> handleWebhook(
//
//            @RequestBody String payload,
//
//            @RequestHeader("Stripe-Signature") String sigHeader
//
//    ){
//
//
//        Event event;
//
//
//
//        try {
//
//
//            event = Webhook.constructEvent(
//
//                    payload,
//
//                    sigHeader,
//
//                    webhookSecret
//
//            );
//
//
//        }
//        catch(Exception e){
//
//            return ResponseEntity
//                    .badRequest()
//                    .body("Invalid webhook");
//
//        }
//
//
//
//        if(
//                "checkout.session.completed"
//                        .equals(event.getType())
//        ){
//
//
//            handlePaymentSuccess(event);
//
//
//        }
//
//
//        return ResponseEntity.ok("received");
//
//
//    }
//
//    private void handlePaymentSuccess(Event event){
//
//
//        EventDataObjectDeserializer deserializer =
//                event.getDataObjectDeserializer();
//
//
//
//        Session session =
//                (Session)
//                        deserializer.getObject()
//                                .get();
//
//
//
//        String paymentLinkId =
//                session.getPaymentLink();
//
//
//
//        Payment payment =
//                paymentRepository
//                        .findByPaymentId(paymentLinkId)
//                        .orElseThrow();
//
//
//
//        payment.setPaymentStatus(
//                PaymentStatus.SUCCESS
//        );
//
//
//
//        paymentRepository.save(payment);
//
//
//
//        Order order =
//                payment.getOrder();
//
//
//
//        order.setOrderStatus(
//                OrderStatus.PAID
//        );
//
//
//
//        orderRepository.save(order);
//
//
//    }
//
//}


