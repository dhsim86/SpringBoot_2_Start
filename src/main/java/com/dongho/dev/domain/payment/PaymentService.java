package com.dongho.dev.domain.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public Payment getAndFoo(Long id) {
        Payment payment = paymentRepository.findById(id).get();

        // foo

        return payment;
    }

}
