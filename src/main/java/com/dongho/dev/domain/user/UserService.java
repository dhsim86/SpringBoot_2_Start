package com.dongho.dev.domain.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Size;

@Slf4j
@Validated
public class UserService {

    public String validateTest(@Size(min = 8, max = 10) String code) {
        log.info("validate Test");
        return code;
    }

}
