package com.dongho.dev.infrastructure.persistence.jpa.implementation;

import com.dongho.dev.domain.payment.Payment;
import com.dongho.dev.domain.payment.PaymentRepositoryCustom;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentRepositoryImpl extends QuerydslRepositorySupport implements PaymentRepositoryCustom {

    public PaymentRepositoryImpl() {
        super(Payment.class);
    }

}
