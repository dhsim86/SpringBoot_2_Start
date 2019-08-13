package com.dongho.dev.web.protocol;

import lombok.Getter;
import lombok.Setter;

import javax.validation.GroupSequence;
import javax.validation.constraints.*;

@Getter
@Setter
@GroupSequence({TestValidationRequest.class, TestValidationRequest.FirstValidation.class, TestValidationRequest.SecondValidation.class})
public class TestValidationRequest {

    interface FirstValidation {

    }

    interface SecondValidation {

    }

    @NotEmpty(message = "The test is required.")
    private String test;

    @NotNull(message = "The executeTest is required.")
    private Boolean executeTest;

    @NotNull(message = "The count is required.")
    @Min(value = 1, message = "The minimum count is one.", groups = FirstValidation.class)
    @Max(value = 100, message = "The maximum count is 100.", groups = FirstValidation.class)
    private Integer count;

    @AssertTrue(message = "invalid request.", groups = SecondValidation.class)
    private boolean isValid() {
        if (executeTest == true) {

            if (test.equals("test") && count == 10) {
                return true;
            }

            return false;
        }

        return true;
    }

}
