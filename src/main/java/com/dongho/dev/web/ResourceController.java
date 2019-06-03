package com.dongho.dev.web;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;

@Controller
public class ResourceController {

    // with no-store, Etag not applied
    @GetMapping("/no-etag/{path}/**")
    public String noEtag(@PathVariable String path, HttpServletResponse response) {
        response.setHeader(HttpHeaders.CACHE_CONTROL, CacheControl.noStore().mustRevalidate().getHeaderValue());
        return "forward:/index.html";
    }

    // with no-cache, Etag applied
    @GetMapping("/etag/{path}/**")
    public String etag(@PathVariable String path, HttpServletResponse response) {
        response.setHeader(HttpHeaders.CACHE_CONTROL, CacheControl.noCache().mustRevalidate().getHeaderValue());
        return "forward:/index.html";
    }

}
