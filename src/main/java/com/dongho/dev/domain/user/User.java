package com.dongho.dev.domain.user;

import javax.persistence.*;

@Entity
@Table(catalog = "test", name = "user")
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private int age;

}
