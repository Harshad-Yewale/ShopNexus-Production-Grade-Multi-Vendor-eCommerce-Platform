package com.harshadcodes.EcommerceWebsite.controller;

import com.harshadcodes.EcommerceWebsite.payload.OrderDTO;
import com.harshadcodes.EcommerceWebsite.payload.OrderRequestDTO;
import com.harshadcodes.EcommerceWebsite.service.OrderService;
import com.harshadcodes.EcommerceWebsite.utils.AuthUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {

    private final AuthUtils authUtils;
    private final OrderService orderService;

    @PostMapping("/order/users/payments/{paymentMethod}")
    public ResponseEntity<OrderDTO> placeOrder(@PathVariable String paymentMethod,
                                               @Valid @RequestBody OrderRequestDTO orderRequestDTO) throws Exception {

        String email=authUtils.getLoggedinEmail();
        OrderDTO orderDTO=orderService.placeOrder(
                email,
                orderRequestDTO.getAddressId(),
                paymentMethod,
                orderRequestDTO.getPgPaymentId(),
                orderRequestDTO.getPgName(),
                orderRequestDTO.getPgStatus(),
                orderRequestDTO.getPgResponseMessage()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(orderDTO);
    }
}
