package com.scaler.myfirstspringbootproj.Service;



import com.scaler.myfirstspringbootproj.DTO.createNewOrderRequest;
import com.scaler.myfirstspringbootproj.ExceptionHandling.CartNotFoundException;
import com.scaler.myfirstspringbootproj.ExceptionHandling.OrderNotFoundException;
import com.scaler.myfirstspringbootproj.ExceptionHandling.UserNotFoundException;
import com.scaler.myfirstspringbootproj.Repository.CartRepository;
import com.scaler.myfirstspringbootproj.Repository.OrderRepository;
import com.scaler.myfirstspringbootproj.Repository.ProductRepository;
import com.scaler.myfirstspringbootproj.Repository.UserRepository;
import com.scaler.myfirstspringbootproj.models.*;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private CartRepository cartRepository;
    private CouponService couponService;



    public OrderService(OrderRepository orderRepository, UserRepository userRepository,
                        ProductRepository productRepository, CartRepository cartRepository, CouponService couponService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.couponService = couponService;

    }

    //get single order
public Order getSingleOrder(Long id) throws OrderNotFoundException {
        Optional<Order> optOrder = orderRepository.findById(id);
        if(optOrder.isEmpty()){
                throw new OrderNotFoundException("The Order with ID: "+id+" is not found in our Database");
        }

        return optOrder.get();
}

    //get all orders
public List<Order> getAllOrdersforaUserId(Long  userId){
        List<Order> orders=orderRepository.findAllByUser_Id(userId);
        return orders;
}

    //create an Order

    public Order createOrder(createNewOrderRequest createNewOrderRequest) {

        //get current user of find user passed by FE
//        User user= userRepository.findById(createNewOrderRequest.getUserId())
//                .orElseThrow(() -> new UserNotFoundException("user not found"));

        //2.  get cart of the user
        Cart cart=cartRepository.findById(createNewOrderRequest.getCartId())
                .orElseThrow(() -> new CartNotFoundException("cart not found"));

        //3.a
        Order newOrder =new Order();
        newOrder.setUser(cart.getUser());
        newOrder.setOrderStatus(OrderStatus.CREATED);


            //4. calculate amount
        double totalamount=0;

        //3. get cartItems from this cart
        List<CartItem> cartItems=cart.getCartItems();


//convert cartItems to Order Items
        for(CartItem cartItem:cartItems){


            Product product=cartItem.getProduct();
            Integer quantity=cartItem.getQuantity();
            totalamount+=product.getPrice() * quantity;

            OrderItem orderItem=new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            orderItem.setPriceAtpurchase(product.getPrice());
            orderItem.setOrder(newOrder);

            newOrder.getOrderItems().add(orderItem);
        }
        newOrder.setAmount(totalamount);

        //5. apply coupon
        if(createNewOrderRequest.getCouponCode()!=null){
            totalamount= couponService.applyCoupon(totalamount, createNewOrderRequest.getCouponCode());
        }

        //7. save to database
        return orderRepository.save(newOrder);

    }



    //update order

    //delete a order

}
