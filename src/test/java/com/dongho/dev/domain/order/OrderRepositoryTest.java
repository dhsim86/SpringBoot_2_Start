package com.dongho.dev.domain.order;

import com.dongho.dev.domain.user.User;
import com.dongho.dev.domain.user.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class OrderRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void findByIdTest() {
        Order order;

        given: {
            User user = userRepository.findById(1).get();
            Order saveOrder = new Order();

            saveOrder.setUser(user);
            orderRepository.save(saveOrder);
        }

        when: {
            order = orderRepository.findById(1).get();
        }

        then: {
            assertThat(order).isNotNull();
            assertThat(order.getUser()).isNotNull();
        }
    }

    @Test
    public void findAllTest() {
        Order order;

        given: {
            User user = userRepository.findById(1).get();
            Order saveOrder = new Order();

            saveOrder.setUser(user);
            orderRepository.save(saveOrder);
        }

        when: {
            order = orderRepository.findAll().get(0);
        }

        then: {
            assertThat(order).isNotNull();
            assertThat(order.getUser()).isNotNull();
        }
    }

}
