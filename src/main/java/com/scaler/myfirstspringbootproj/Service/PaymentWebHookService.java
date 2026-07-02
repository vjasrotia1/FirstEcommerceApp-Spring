package com.scaler.myfirstspringbootproj.Service;

import com.scaler.myfirstspringbootproj.ExceptionHandling.PaymentNotFoundException;
import com.scaler.myfirstspringbootproj.Repository.OrderRepository;
import com.scaler.myfirstspringbootproj.Repository.PaymentRepository;
import com.scaler.myfirstspringbootproj.models.Order;
import com.scaler.myfirstspringbootproj.models.OrderStatus;
import com.scaler.myfirstspringbootproj.models.Payment;
import com.scaler.myfirstspringbootproj.models.PaymentStatus;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentWebHookService {

    private PaymentRepository paymentRepository;
    private OrderRepository orderRepository;

    public PaymentWebHookService(PaymentRepository paymentRepository, OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }


    @Transactional
    public void handlePaymentSuccess(Event event){


        EventDataObjectDeserializer deserializer =
                event.getDataObjectDeserializer();

        StripeObject stripeObject=deserializer.getObject()
                .orElseThrow(
                        () -> new RuntimeException("Unable to deserialize Stripe event")
                );

        if(stripeObject==null){
            return;
        }

        Session session =
                (Session) stripeObject;



        String paymentLinkId =
                session.getPaymentLink();



        Payment payment =
                paymentRepository
                        .findByPaymentId(paymentLinkId)
                        .orElseThrow(
                                () -> new PaymentNotFoundException("payment not found for the payment link: "+paymentLinkId)
                        );

        payment.setPaymentStatus(
                PaymentStatus.SUCCESS
        );

        paymentRepository.save(payment);

        Order order =
                payment.getOrder();

        order.setOrderStatus(
                OrderStatus.PAID
        );

        orderRepository.save(order);
    }
}
