package com.dongho.dev.domain.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import javax.persistence.*;

@Configurable(value = "user", autowire = Autowire.BY_TYPE, dependencyCheck = true, preConstruction = true)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Entity
@Getter
@Setter
public class User {

    @Transient
    @Autowired
    private UserRepository repository;

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private int age;

    public User save() {
        return repository.save(this);
    }

}
