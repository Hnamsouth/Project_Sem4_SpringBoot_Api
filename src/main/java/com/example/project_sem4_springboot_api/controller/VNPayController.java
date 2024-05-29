package com.example.project_sem4_springboot_api.controller;

import com.example.project_sem4_springboot_api.entities.enums.EStatus;
import com.example.project_sem4_springboot_api.entities.enums.PaymentMethod;
import com.example.project_sem4_springboot_api.entities.request.VnPayRequest;
import com.example.project_sem4_springboot_api.repositories.StudentTransactionRepository;
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

    private final StudentTransactionRepository studentTransactionRepository;


    @PostMapping("/submitOrder")
    public String submidOrder(@RequestBody VnPayRequest data,
                              HttpServletRequest request) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        return vnPayService.createOrder(data.getTotal(), data.getOrderInfo(), baseUrl);
    }

    @GetMapping("/vnpay-payment")
    public ResponseEntity<?> GetMapping(HttpServletRequest request, Model model) {
        int paymentStatus = vnPayService.orderReturn(request);
        String orderId = request.getParameter("vnp_OrderInfo");
        if(paymentStatus ==1 ){
            // thanh toán thành công
            var stdTrans = studentTransactionRepository.findById(Long.parseLong(orderId));
            if(stdTrans.isPresent()){
                var std = stdTrans.get();
                std.setPaid(Double.parseDouble(request.getParameter("vnp_Amount")));
                std.setStatus(EStatus.STUDENT_TRANS_PAID.getName());
                std.setStatusCode(EStatus.STUDENT_TRANS_PAID);
                std.setPaymentMethod(PaymentMethod.CHUYEN_KHOAN);
                studentTransactionRepository.save(std);
            }
            model.addAttribute("orderId", orderId);
            model.addAttribute("totalPrice", request.getParameter("vnp_Amount"));
            model.addAttribute("paymentTime", request.getParameter("vnp_PayDate"));
            model.addAttribute("transactionId", request.getParameter("vnp_TransactionNo"));
            return ResponseEntity.ok("Thanh toán thành công");
        }
        return ResponseEntity.ok("Giao dịch thất bại");
    }
}