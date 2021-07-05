package com.dongho.dev.application;

import com.dongho.dev.domain.payment.Payment;
import com.dongho.dev.domain.payment.PaymentRepository;
import com.dongho.dev.domain.payment.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentAppService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentService paymentService;

    @Transactional
    public Payment approve(long amount) {
        Payment payment = new Payment(amount);

        paymentRepository.save(payment);
        return paymentService.getAndFoo(payment.getId());
    }

    public Payment approveWithoutTransactional(long amount) {
        Payment payment = new Payment(amount);

        paymentRepository.save(payment);
        return paymentService.getAndFoo(payment.getId());
    }

}
