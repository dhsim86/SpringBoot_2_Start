package com.dongho.dev.web;

import com.dongho.dev.web.protocol.ListValidationRequest;
import com.dongho.dev.web.protocol.TestValidationRequest;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/list-validation")
    public String listValidation(@Valid @RequestBody ListValidationRequest request) {
        return request.getElementList().get(0).getTestString();
    }

}
