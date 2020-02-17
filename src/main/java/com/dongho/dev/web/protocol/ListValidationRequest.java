package com.dongho.dev.web.protocol;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
public class ListValidationRequest {

    @NotEmpty(message = "testString is required.")
    private String testString;

    @Valid
    @NotEmpty(message = "elementList is required")
    private List<Element> elementList;

    @Getter
    @Setter
    public static class Element {

        @NotEmpty(message = "testString is required.")
        private String testString;

    }

}
