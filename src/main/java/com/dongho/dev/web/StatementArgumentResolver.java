package com.dongho.dev.web;

import com.dongho.dev.web.protocol.StatementParameter;
import org.springframework.core.MethodParameter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

public class StatementArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return StatementParameter.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Mono<Object> resolveArgument(
            MethodParameter parameter, BindingContext bindingContext, ServerWebExchange exchange) {

        ServerHttpRequest request = exchange.getRequest();

        MultiValueMap<String, String> parameterMap = request.getQueryParams();

        List<String> statementList = parameterMap.entrySet().stream()
            .map(entry -> entry.getKey() + '=' + entry.getValue().get(0))
            .collect(Collectors.toList());

        return Mono.fromCallable(() -> new StatementParameter(statementList));
    }

}
