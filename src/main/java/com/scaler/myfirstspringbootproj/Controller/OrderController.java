package com.scaler.myfirstspringbootproj.Controller;

import com.scaler.myfirstspringbootproj.DTO.CreateOrderDto;
import com.scaler.myfirstspringbootproj.DTO.ErrorDto;
import com.scaler.myfirstspringbootproj.DTO.createNewOrderRequest;
import com.scaler.myfirstspringbootproj.ExceptionHandling.OrderNotFoundException;
import com.scaler.myfirstspringbootproj.Service.OrderService;
import com.scaler.myfirstspringbootproj.models.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

private OrderService orderService;

public OrderController(OrderService orderService){
    this.orderService=orderService;
}

//get single order API

    @GetMapping({"/{orderId}"})
    public ResponseEntity<Order> getSingleOrder(@PathVariable("id") Long id) throws OrderNotFoundException {

    Order order=orderService.getSingleOrder(id);
    return new ResponseEntity<>(order, HttpStatus.OK);

    }


    //get all orders

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders(Long UserId){

    List<Order> orders=orderService.getAllOrdersforaUserId(UserId);
    return new  ResponseEntity<>(orders, HttpStatus.OK);

    }

    //create order

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody CreateOrderDto createOrderDto) {
        //System.out.println(createOrderDto.getCartId());

    createNewOrderRequest createNewOrderRequest = new  createNewOrderRequest(
            //createOrderDto.getUserId(),
            createOrderDto.getCartId(),
            createOrderDto.getShippingAddressId(),
            createOrderDto.getPaymentMethod(),
            createOrderDto.getCouponCode()
    );

            Order order= orderService.createOrder(createNewOrderRequest);

    return new ResponseEntity<>(order, HttpStatus.CREATED);

    }

    //update order

    //delete order


    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorDto> handleOrderNotFoundException(Exception e){
        ErrorDto errorDTO;
        errorDTO = new ErrorDto();
        errorDTO.setMessage(e.getMessage());

        return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
    }

}
