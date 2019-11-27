package com.dongho.dev.domain.item;

import javax.persistence.*;

@Entity
public class Item {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "item_name")
    private String name;

    @Column(name = "item_price")
    private int price;

}
