package com.dongho.dev.domain.user;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {

    @Override
    List<User> findAll();

}
