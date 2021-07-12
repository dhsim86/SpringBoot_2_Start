package com.dongho.dev.web.controller;

import com.dongho.dev.application.PaymentAppService;
import com.dongho.dev.domain.payment.Payment;
import com.dongho.dev.domain.payment.PaymentRepository;
import com.dongho.dev.domain.payment.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Slf4j
@RestController
public class TestController {

    @Autowired
    private PaymentAppService paymentAppService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentRepository paymentRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/test/osiv/payment")
    public Payment getPayment(@RequestParam("amount") long amount) {
        return paymentAppService.approve(amount);
    }

    @GetMapping("/test/osiv/payment-without")
    public Payment getPaymentWithoutTransaction(@RequestParam("amount") long amount) {
        return paymentAppService.approveWithoutTransactional(amount);
    }

    @GetMapping("/test/osiv/check-persist/requires_new")
    public void checkPersistRequiresNew(@RequestParam("id") long id) {
        Optional<Payment> paymentOptional = paymentRepository.findById(id);

        log.info("checkPersist, delegate: {}", entityManager.getDelegate());
        if (paymentOptional.isPresent()) {
            log.info("checkPersist, payment {}", paymentOptional.get());
        }

        paymentAppService.outerTransaction(id);
    }

    @GetMapping("/test/osiv/check-persist/requires")
    public void checkPersistRequires(@RequestParam("id") long id) {
        Optional<Payment> paymentOptional = paymentRepository.findById(id);

        log.info("checkPersist, delegate: {}", entityManager.getDelegate());
        if (paymentOptional.isPresent()) {
            log.info("checkPersist, payment {}", paymentOptional.get());
        }

        paymentService.innerTransaction(id);
    }

}
