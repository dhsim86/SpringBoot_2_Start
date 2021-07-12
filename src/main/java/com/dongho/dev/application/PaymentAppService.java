package com.dongho.dev.application;

import com.dongho.dev.domain.payment.Payment;
import com.dongho.dev.domain.payment.PaymentRepository;
import com.dongho.dev.domain.payment.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Slf4j
@Service
public class PaymentAppService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentService paymentService;

    @PersistenceContext
    private EntityManager entityManager;

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

    @Transactional
    public void outerTransaction(Long id) {
        log.info("started outerTransaction");

        log.info("outerTransaction, delegate: {}", entityManager.getDelegate());
        log.info("outerTransaction, payment: {}", entityManager.find(Payment.class, id));
        paymentService.innerTransaction(id);

        log.info("completed outerTransaction");
    }

}
