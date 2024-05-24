package com.example.project_sem4_springboot_api.controller;

import com.example.project_sem4_springboot_api.service.impl.VnPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
 class VNPayController {

    private final VnPayService vnPayService;

    public VNPayController(VnPayService vnPayService) {
        this.vnPayService = vnPayService;
    }

    @GetMapping("/create_payment")
    public String createPayment(@RequestParam String amount, @RequestParam String orderInfo) {
        return vnPayService.createPaymentRequest(amount, orderInfo);
    }

    @GetMapping("/vnpay_return")
    public String vnPayReturn(HttpServletRequest request) {
        // Xử lý kết quả trả về từ VNPay
        return "Payment Successful";
    }
}
