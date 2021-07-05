package com.dongho.dev.config;


import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Component
@ConfigurationPropertiesBinding
public class DateTimePropertyConverter implements Converter<String, LocalDateTime> {

    @Override
    public LocalDateTime convert(String source) {
        if (StringUtils.isEmpty(source) == true) {
            return null; // Since the validation is processed in @ConfigurationProperties class.
        }

        return LocalDateTime.parse(source);
    }

}
