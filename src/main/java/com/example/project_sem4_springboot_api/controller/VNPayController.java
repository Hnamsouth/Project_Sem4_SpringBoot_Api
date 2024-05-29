package com.example.project_sem4_springboot_api.controller;

import com.example.project_sem4_springboot_api.entities.request.VnPayRequest;
import com.example.project_sem4_springboot_api.service.impl.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController

@RequestMapping("/api/v1/vn-pay")
@RequiredArgsConstructor
class VNPayController {
    @Autowired
    private VNPayService vnPayService;


    @PostMapping("/submitOrder")
    public String submidOrder(@RequestBody VnPayRequest data,
                              HttpServletRequest request) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        return vnPayService.createOrder(data.getTotal(), data.getOrderInfo(), baseUrl);
    }

    @GetMapping("/vnpay-payment")
    public ResponseEntity<?> GetMapping(HttpServletRequest request, Model model) {
        int paymentStatus = vnPayService.orderReturn(request);


        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("paymentTime", paymentTime);
        model.addAttribute("transactionId", transactionId);
        return ResponseEntity.ok( paymentStatus == 1 ? model:null);
//        return paymentStatus == 1 ? "ordersuccess" : "orderfail";
    }
}