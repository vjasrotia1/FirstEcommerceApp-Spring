package com.scaler.myfirstspringbootproj.Controller;


import com.scaler.myfirstspringbootproj.DTO.AddCartItemRequestDto;
import com.scaler.myfirstspringbootproj.ExceptionHandling.ProductNotFoundException;
import com.scaler.myfirstspringbootproj.Service.CartService;
import com.scaler.myfirstspringbootproj.models.CartItem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {

    private CartService cartService;
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }
    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItem> addItemToCart(
            @PathVariable Long cartId, @RequestBody AddCartItemRequestDto addCartItemRequestDto) throws ProductNotFoundException {
CartItem cartItem = cartService.addItemToCart(
        cartId,addCartItemRequestDto
);

        return new ResponseEntity<>(cartItem, HttpStatus.CREATED);

    }
}
