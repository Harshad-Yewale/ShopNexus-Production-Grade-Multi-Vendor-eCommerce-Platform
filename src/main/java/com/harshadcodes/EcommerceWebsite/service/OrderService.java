package com.harshadcodes.EcommerceWebsite.service;

import com.harshadcodes.EcommerceWebsite.payload.OrderDTO;
import jakarta.transaction.Transactional;


public interface OrderService {

    @Transactional
    OrderDTO placeOrder(String email, Long addressId, String paymentMethod, String pgPaymentId, String pgName, String pgStatus, String pgResponseMessage) throws Exception;
}
