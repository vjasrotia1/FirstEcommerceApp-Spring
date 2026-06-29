package com.scaler.myfirstspringbootproj.Service;

import com.scaler.myfirstspringbootproj.Repository.CouponRepository;
import com.scaler.myfirstspringbootproj.models.Coupon;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CouponService {

    private CouponRepository couponRepository;

    public CouponService(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    public Double applyCoupon(Double amount, String couponCode) {

        Coupon coupon = couponRepository.findByCode(couponCode)
                .orElseThrow(() -> new RuntimeException("invalid coupon code"));

        //check active
        if(!coupon.getActive()){
            throw new RuntimeException("coupon expired");
        }

        //check expiry date

        if(coupon.getExpiryDate().isBefore(LocalDate.now())){
            throw new RuntimeException("coupon expired");
        }

        //calculate discount

        Double discountAmount=amount*coupon.getDiscount()/100;

        return amount-discountAmount;
    }
}
