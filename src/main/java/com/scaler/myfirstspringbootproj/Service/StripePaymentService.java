package com.scaler.myfirstspringbootproj.Service;


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

    @Value("${stripe.api.key}")
private String stripeApiKey;

    @Override
    public String makePayment(Long orderId, Long orderAmount) throws StripeException {

        Stripe.apiKey = stripeApiKey;

        //make price params . OR what are the params required to create Price

        PriceCreateParams params= PriceCreateParams.builder()
                .setCurrency("inr")
                .setUnitAmount(orderAmount)
                .setProductData(
                        PriceCreateParams.ProductData.builder().setName(""+orderId).build()
                )
                .putMetadata("OrderId",String.valueOf(orderId))
                .build();

        //create Price Object of Stripe
        Price price= Price.create(params);

        // next pass id returned by price to the createPaymentLink
        PaymentLinkCreateParams paymentLinkParams =
                PaymentLinkCreateParams.builder()
                        .addLineItem(
                                PaymentLinkCreateParams.LineItem.builder()
                                        .setPrice(price.getId())
                                        .setQuantity(1L)
                                        .build()
                        )
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

        return paymentLink.getUrl();

    }
}
