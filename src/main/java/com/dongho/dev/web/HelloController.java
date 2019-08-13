package com.dongho.dev.web;

import com.dongho.dev.web.protocol.TestValidationRequest;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@EnableAutoConfiguration
public class HelloController {

    @RequestMapping("/")
    String home() {
        return "Hello World!";
    }

    @GetMapping("/test-validation")
    public String testValidation(@Valid @ModelAttribute TestValidationRequest request) {
        return request.getTest();
    }

}
