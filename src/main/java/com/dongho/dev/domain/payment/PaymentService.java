package com.dongho.dev.domain.payment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Slf4j
@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public Payment getAndFoo(Long id) {
        Payment payment = paymentRepository.findById(id).get();

        // foo

        return payment;
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public void innerTransaction(Long id) {
        log.info("started innerTransaction");

        log.info("innerTransaction, delegate: {}", entityManager.getDelegate());
        log.info("innerTransaction, payment: {}", entityManager.find(Payment.class, id));

        log.info("completed innerTransaction");
    }

}
