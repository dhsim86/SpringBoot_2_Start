package com.dongho.dev.domain.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void userDependencyTest() {
        User user;

        given: {
        }

        when: {
            user = new User();
        }

        then: {
            assertThat(user.getRepository()).isNotNull();
        }
    }

    @Test
    public void saveItselfTest() {
        User user;

        given: {
            user = new User();
            user.setId(0);
            user.setName("saveItself");
            user.setAge(10);
        }

        when: {
            user = user.save();
        }

        then: {
            Optional<User> optUser = userRepository.findById(user.getId());
            assertThat(optUser.isPresent()).isTrue();
            assertThat(optUser.map(User::getId).get()).isEqualTo(user.getId());
            assertThat(optUser.map(User::getName).get()).isEqualTo(user.getName());
            assertThat(optUser.map(User::getAge).get()).isEqualTo(user.getAge());
        }
    }

}
