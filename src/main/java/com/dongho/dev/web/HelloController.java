package com.dongho.dev.web;

import antlr.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@EnableAutoConfiguration
public class HelloController {

    @RequestMapping("/")
    String home() {
        return "jenkins webhook test2!";
    }

    @GetMapping("/optional/test")
    public String optionalTest() {
        String notEmpty = "notEmpty";
        String empty = "";

        String result0 = Optional.ofNullable(notEmpty)
            .filter(s -> s.isEmpty() == false)
            .map(String::toUpperCase)
            .orElse("test");

        String result1 = Optional.ofNullable(empty)
            .filter(s -> s.isEmpty() == false)
            .map(String::toUpperCase)
            .orElse("test");

        String result2 = Optional.<String>empty()
            .orElse("test");

        log.info("result0: {}", result0);
        log.info("result1: {}", result1);
        log.info("result2: {}", result2);

        //2019-12-17 12:18:53 | INFO  | HelloController                    :40   | [http-nio-8080-exec-1] result0: NOTEMPTY
        //2019-12-17 12:18:53 | INFO  | HelloController                    :41   | [http-nio-8080-exec-1] result1: test
        //2019-12-17 12:18:53 | INFO  | HelloController                    :42   | [http-nio-8080-exec-1] result2: test

        return "";
    }

}
