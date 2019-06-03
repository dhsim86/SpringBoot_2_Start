package com.dongho.dev.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private static final String RESOURCE_MAPPING_LOCATION_FROM = "/**";
    private static final String RESOURCE_MAPPING_LOCATION_TO = "classpath:/public/";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(RESOURCE_MAPPING_LOCATION_FROM)
                .addResourceLocations(RESOURCE_MAPPING_LOCATION_TO);
    }

    @Bean
    public FilterRegistrationBean etagRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();

        registration.setFilter(new ShallowEtagHeaderFilter());
        registration.setUrlPatterns(Arrays.asList("/*"));

        return registration;
    }

}
