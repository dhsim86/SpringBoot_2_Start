package com.dongho.dev.domain.user;

import lombok.SneakyThrows;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Before
    @Rollback(false)
    @SneakyThrows
    public void prepare() {
        Connection connection = DriverManager.getConnection(
            "jdbc:mysql://10.32.195.52:5605?useUnicode=true&characterEncoding=UTF-8&useSSL=true&verifyServerCertificate=false",
            "tc_rds",
            "rds!#%135"
        );
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + "test");
        stmt.executeUpdate("CREATE TABLE test.user (user_id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(250) NOT NULL, age INT NOT NULL)");
        stmt.executeUpdate("INSERT INTO test.user (name, age) VALUES ('member_00', 15)");
        stmt.executeUpdate("INSERT INTO test.user (name, age) VALUES ('member_01', 16)");

        stmt.close();
        connection.close();
    }

    @After
    @Rollback(false)
    @SneakyThrows
    public void clean() {
        Connection connection = DriverManager.getConnection(
            "jdbc:mysql://10.32.195.52:5605?useUnicode=true&characterEncoding=UTF-8&useSSL=true&verifyServerCertificate=false",
            "tc_rds",
            "rds!#%135"
        );
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DROP TABLE test.user");
        stmt.executeUpdate("DROP DATABASE test");

        stmt.close();
        connection.close();
    }

    @Test
    public void findTest() {
        List<User> userList = userRepository.findAll();
        assertThat(userList).isNotEmpty().hasSize(2);
    }

}
