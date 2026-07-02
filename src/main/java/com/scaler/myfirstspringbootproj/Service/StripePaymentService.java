package com.scaler.myfirstspringbootproj.Service;


import com.scaler.myfirstspringbootproj.DTO.CreatePaymentRequest;
import com.scaler.myfirstspringbootproj.DTO.PaymentResponseDto;
import com.scaler.myfirstspringbootproj.ExceptionHandling.OrderNotFoundException;
import com.scaler.myfirstspringbootproj.Repository.OrderRepository;
import com.scaler.myfirstspringbootproj.Repository.PaymentRepository;
import com.scaler.myfirstspringbootproj.models.Order;
import com.scaler.myfirstspringbootproj.models.Payment;
import com.scaler.myfirstspringbootproj.models.PaymentStatus;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentLink;
import com.stripe.model.Price;
import com.stripe.param.PaymentLinkCreateParams;
import com.stripe.param.PriceCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripePaymentService implements PaymentService {


    private String stripeApiKey;
    private OrderRepository  orderRepository;
    private PaymentRepository paymentRepository;


    public StripePaymentService(@Value("${stripe.api.key}") String stripeApiKey,OrderRepository orderRepository,PaymentRepository paymentRepository) {
        this.stripeApiKey = stripeApiKey;
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;

    }

    @Override
    public PaymentResponseDto createPayment(CreatePaymentRequest createPaymentRequest) throws StripeException {

        Stripe.apiKey = stripeApiKey;

        //1. fetch Order from order Repository
        Order order=orderRepository.findById(createPaymentRequest.getOrderId())
                .orElseThrow(() -> new OrderNotFoundException("Order with id " +createPaymentRequest.getOrderId() + " not found in DB"));

        //2. get amount from database
        Double amount= order.getAmount();


        //3. make price params . OR what are the params required to create Price

        PriceCreateParams params= PriceCreateParams.builder()
                .setCurrency("inr")
                .setUnitAmount((long)(amount*100))
                .setProductData(
                        //idempotent key
                        PriceCreateParams.ProductData.builder().setName("order Payment "+order.getId()).build()
                )
                .putMetadata("OrderId",String.valueOf(order.getId()))
                .build();


        //4. create Price Object in Stripe
        Price price= Price.create(params);

        // 5. next pass Price id(of the price Object created above in step 4)  returned by price to the createPaymentLink
        PaymentLinkCreateParams paymentLinkParams =
                PaymentLinkCreateParams.builder()
                        .addLineItem(
                                PaymentLinkCreateParams.LineItem.builder()
                                        .setPrice(price.getId())
                                        .setQuantity(1L)
                                        .build()
                        )
                        .putMetadata(
                                "orderId",String.valueOf(order.getId())
                        )
                        //below is kind of functional callback
                        .setAfterCompletion(
                                PaymentLinkCreateParams.AfterCompletion.builder()
                                        .setType(PaymentLinkCreateParams.AfterCompletion.Type.REDIRECT)
                                        .setRedirect(
                                                PaymentLinkCreateParams.AfterCompletion.Redirect.builder()
                                                        .setUrl("https://google.com")
                                                        .build()
                                        )
                                        .build()
                        )
                        .build();


        PaymentLink paymentLink = PaymentLink.create(paymentLinkParams);

        Payment payment = new Payment();


        payment.setOrder(order);


        payment.setPaymentId(
                paymentLink.getId()
        );


        payment.setPaymentLink(
                paymentLink.getUrl()
        );


        payment.setAmount(
                order.getAmount()
        );


        payment.setPaymentStatus(
                PaymentStatus.CREATED
        );


        paymentRepository.save(payment);

        return new PaymentResponseDto(order.getId(),
                paymentLink.getUrl(),
                paymentLink.getId(),
                order.getAmount(),
                PaymentStatus.CREATED
        );

    }
}
