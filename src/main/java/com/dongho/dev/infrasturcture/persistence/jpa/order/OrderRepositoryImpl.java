package com.dongho.dev.infrasturcture.persistence.jpa.order;

import com.dongho.dev.domain.order.Order;
import com.dongho.dev.domain.order.OrderRepositoryCustom;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class OrderRepositoryImpl extends QuerydslRepositorySupport implements OrderRepositoryCustom {

    public OrderRepositoryImpl() {
        super(Order.class);
    }

}
