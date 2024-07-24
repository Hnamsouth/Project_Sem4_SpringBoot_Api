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
    public String submidOrder(
            @RequestBody VnPayRequest data,
            HttpServletRequest request
    ){
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        return vnPayService.createOrder(data.getTotal(), data.getOrderInfo(), baseUrl);
    }

    @GetMapping("/vnpay-payment")
    public ResponseEntity<?> GetMapping(HttpServletRequest request, Model model) {
        int paymentStatus = vnPayService.orderReturn(request);
        String orderId = request.getParameter("vnp_OrderInfo");
        if(paymentStatus == 1 ){
            // thanh toán thành công
            var stdTrans = studentTransactionRepository.findById(Long.parseLong(orderId));
            if(stdTrans.isPresent()){
                var paid = Double.parseDouble(request.getParameter("vnp_Amount"));
                var std = stdTrans.get();
                std.setPaid(paid/100);
                std.setStatus(EStatus.STUDENT_TRANS_PAID.getName());
                std.setStatusCode(EStatus.STUDENT_TRANS_PAID);
                std.setPaymentMethod(PaymentMethod.CHUYEN_KHOAN);
                studentTransactionRepository.save(std);
            }
            model.addAttribute("orderId", orderId);
            model.addAttribute("totalPrice", request.getParameter("vnp_Amount"));
            model.addAttribute("paymentTime", request.getParameter("vnp_PayDate"));
            model.addAttribute("transactionId", request.getParameter("vnp_TransactionNo"));
            return ResponseEntity.ok("""
                    <!DOCTYPE html>
                    <html lang="en">
                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <title>Transaction Success</title>
                        <style>
                        * {
                            margin: 0;
                            padding: 0;
                            box-sizing: border-box;
                        }
                        
                        body {
                            font-family: Arial, sans-serif;
                            background-color: #f4f4f4;
                            display: flex;
                            align-items: center;
                            justify-content: center;
                            height: 100vh;
                            margin: 0;
                        }
                        
                        .container {
                            background: #fff;
                            padding: 20px;
                            border-radius: 10px;
                            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
                            text-align: center;
                            max-width: 90%;
                        }
                        
                        .success-icon {
                            margin-bottom: 20px;
                        }
                        
                        h1 {
                            color: #28a745;
                            font-size: 24px;
                            margin-bottom: 10px;
                        }
                        
                        p {
                            font-size: 16px;
                            color: #555;
                            margin-bottom: 20px;
                        }
                        
                        .button {
                            display: inline-block;
                            padding: 10px 20px;
                            font-size: 16px;
                            color: #fff;
                            background-color: #28a745;
                            border: none;
                            border-radius: 5px;
                            text-decoration: none;
                        }
                        
                        .button:hover {
                            background-color: #218838;
                        }
                        </style>
                    </head>
                    <body>
                        <div class="container">
                            <div class="success-icon">
                                <svg xmlns="http://www.w3.org/2000/svg" width="100" height="100" fill="green" class="bi bi-check-circle-fill" viewBox="0 0 16 16">
                                    <path d="M16 8a8 8 0 1 0-16 0 8 8 0 0 0 16 0zM6.904 10.803a.25.25 0 0 1-.348-.01L4.146 8.264a.25.25 0 1 1 .348-.35l2.096 2.097 4.657-4.657a.25.25 0 0 1 .348.35l-5 5a.25.25 0 0 1-.348.01z"/>
                                </svg>
                            </div>
                            <h1>Thanh toán thành công </h1>
                            <p>Giao dịch của bạn đã hoàn tất.</p>
                            <button class="back-button" onclick="goBackToApp()">Quay lại ứng dụng</button>
                            <script>
                                    function goBackToApp() {
                                        var appURL = 'com.project4.hs.2204m'; // Thay thế bằng URL scheme của bạn
                                        window.location.href = appURL;
                                    }
                                </script>
                        </div>
                    </body>
                    </html>
                                        
                    """);
        }
        return ResponseEntity.ok("Giao dịch thất bại");
    }
}