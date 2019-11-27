package com.dongho.dev.domain.order;

import com.dongho.dev.domain.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "order_list")
public class Order {

    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Getter(AccessLevel.PACKAGE)
    @ManyToOne
    private User user;

}
