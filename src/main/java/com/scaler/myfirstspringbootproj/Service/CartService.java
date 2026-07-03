package com.scaler.myfirstspringbootproj.Service;

import com.scaler.myfirstspringbootproj.DTO.AddCartItemRequestDto;
import com.scaler.myfirstspringbootproj.ExceptionHandling.CartNotFoundException;
import com.scaler.myfirstspringbootproj.ExceptionHandling.ProductNotFoundException;
import com.scaler.myfirstspringbootproj.Repository.CartItemRepository;
import com.scaler.myfirstspringbootproj.Repository.CartRepository;
import com.scaler.myfirstspringbootproj.Repository.ProductRepository;
import com.scaler.myfirstspringbootproj.models.Cart;
import com.scaler.myfirstspringbootproj.models.CartItem;
import com.scaler.myfirstspringbootproj.models.Product;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private CartItemRepository cartItemRepository;
    private CartRepository cartRepository;
    private ProductRepository productRepository;

public CartService(CartItemRepository cartItemRepository, CartRepository cartRepository, ProductRepository productRepository) {
    this.cartItemRepository = cartItemRepository;
    this.cartRepository = cartRepository;
    this.productRepository = productRepository;
}

public CartItem addItemToCart(Long cartId, AddCartItemRequestDto  addCartItemRequestDto) throws ProductNotFoundException {

    Cart cart= cartRepository.findById(cartId)
            .orElseThrow(() -> new CartNotFoundException("Cart not found"));

    Product product = productRepository.findById(addCartItemRequestDto.getProductId())
            .orElseThrow(() -> new ProductNotFoundException("no product found"));

CartItem cartItem=new CartItem();

cartItem.setCart(cart);
    cartItem.setProduct(product);
    cartItem.setQuantity(addCartItemRequestDto.getQuantity());

    return cartItemRepository.save(cartItem);

}

}
