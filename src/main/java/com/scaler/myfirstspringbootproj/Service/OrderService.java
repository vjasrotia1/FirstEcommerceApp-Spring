package com.scaler.myfirstspringbootproj.Service;



import com.scaler.myfirstspringbootproj.DTO.createNewOrderRequest;
import com.scaler.myfirstspringbootproj.ExceptionHandling.OrderNotFoundException;
import com.scaler.myfirstspringbootproj.Repository.CartRepository;
import com.scaler.myfirstspringbootproj.Repository.OrderRepository;
import com.scaler.myfirstspringbootproj.Repository.ProductRepo;
import com.scaler.myfirstspringbootproj.Repository.UserRepository;
import com.scaler.myfirstspringbootproj.models.*;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private ProductRepo productRepo;
    private CartRepository cartRepository;
    private CouponService couponService;



    public OrderService(OrderRepository orderRepository, UserRepository userRepository,
                        ProductRepo productRepo, CartRepository cartRepository, CouponService couponService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepo = productRepo;
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

//get current user
        User user= userRepository.findById(createNewOrderRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("user not found"));

        //2.  get cart
        Cart cart=cartRepository.findById(createNewOrderRequest.getCartId())
                .orElseThrow(() -> new RuntimeException("cart not found"));

        //3. get products from cart
        List<Product> products=cart.getProducts();

        //4. calculate amount
        double totalamount=0;
        for(Product product:products){
            totalamount+=product.getPrice();
        }

        //5. apply coupon
        if(createNewOrderRequest.getCouponCode()!=null){
            totalamount= couponService.applyCoupon(totalamount, createNewOrderRequest.getCouponCode());
        }

        //6. create order object
        Order newOrder =new Order();
        newOrder.setUser(user);
        newOrder.setProducts(products);
        newOrder.setAmount(totalamount);
        newOrder.setOrderStatus(OrderStatus.CREATED);


        //7. save to database
        Order savedOrder=orderRepository.save(newOrder);
        return savedOrder;

    }



    //update order

    //delete a order

}
