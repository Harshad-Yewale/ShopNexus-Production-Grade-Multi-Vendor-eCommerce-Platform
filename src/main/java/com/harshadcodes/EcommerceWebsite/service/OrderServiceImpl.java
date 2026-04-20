package com.harshadcodes.EcommerceWebsite.service;

import com.harshadcodes.EcommerceWebsite.exceptions.ResourceNotFoundException;
import com.harshadcodes.EcommerceWebsite.model.*;
import com.harshadcodes.EcommerceWebsite.payload.OrderDTO;
import com.harshadcodes.EcommerceWebsite.payload.OrderItemDTO;
import com.harshadcodes.EcommerceWebsite.repositories.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final CartRepository cartRepository;
    private final AddressRepository addressRepository;
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final ModelMapper modelMapper;


    @Transactional
    @Override
    public OrderDTO placeOrder(String email, Long addressId, String paymentMethod, String pgPaymentId, String pgName, String pgStatus, String pgResponseMessage) throws Exception {
        Cart cart=cartRepository.findCartByEmail(email);

        if(cart==null){
            throw new ResourceNotFoundException("Cart","email",email);
        }
        if(!cart.getUser().getEmail().equals(email)){
            throw new Exception("unauthorized access!!");
        }

        Address address=addressRepository.findById(addressId)
                .orElseThrow(()->new ResourceNotFoundException("Address","addressId",addressId));

        Order order=new Order();
        order.setEmail(email);
        order.setAddress(address);
        order.setOrderDate(LocalDate.now());
        order.setTotalAmount(cart.getTotalPrice());
        order.setOrderStatus("Accepted");

        Payment payment=new Payment(paymentMethod,pgPaymentId,pgStatus,pgResponseMessage,pgName);
        payment.setOrder(order);
        payment=paymentRepository.save(payment);
        order.setPayment(payment);

        Order savedOrder=orderRepository.save(order);

        List<CartItem>cartItems=cart.getCartItems();

        if(cartItems.isEmpty()){
            throw new Exception("cart is empty");
        }

        List<OrderItem> orderItems=new ArrayList<>();

        for (CartItem cartItem : cartItems){
            OrderItem orderItem=new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setOrder(savedOrder);
            orderItem.setOrderItemQuantity(cartItem.getQuantity());
            orderItem.setDiscount(cartItem.getDiscount());
            orderItem.setDiscountedPrice(cartItem.getDiscountedPrice());
            orderItems.add(orderItem);
        }

        orderItems=orderItemRepository.saveAll(orderItems);

        cart.getCartItems().forEach(cartItem -> {
            int quantity=cartItem.getQuantity();
            Product product=cartItem.getProduct();

            product.setProductQuantity(product.getProductQuantity()-quantity);
            productRepository.save(product);

            cartService.deleteCartItem(product.getProductId(),cart.getCartId());

        });

        OrderDTO orderDTO=modelMapper.map(order,OrderDTO.class);
        orderItems
                .forEach(orderItem -> orderDTO.getOrderItems().add(modelMapper.map(orderItem, OrderItemDTO.class)));

        orderDTO.setAddressId(addressId);
        return orderDTO;
    }
}
