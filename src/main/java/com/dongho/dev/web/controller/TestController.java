package com.dongho.dev.web.controller;

import com.dongho.dev.application.PaymentAppService;
import com.dongho.dev.domain.payment.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private PaymentAppService paymentAppService;

    @GetMapping("/test/osiv/payment")
    public Payment getPayment(@RequestParam("amount") long amount) {
        return paymentAppService.approve(amount);
    }

    @GetMapping("/test/osiv/payment-without")
    public Payment getPaymentWithoutTransaction(@RequestParam("amount") long amount) {
        return paymentAppService.approveWithoutTransactional(amount);
    }

}
