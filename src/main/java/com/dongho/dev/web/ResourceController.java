package com.dongho.dev.web;

import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
public class ResourceController {

    @GetMapping("/no-store")
    public String noStore(HttpServletResponse response) {
        // with no-store, Etag not applied
        response.setHeader(HttpHeaders.CACHE_CONTROL, CacheControl.noStore().mustRevalidate().getHeaderValue());
        return "forward:/index.html";
    }

    @GetMapping("/no-cache")
    public String noCache() {
        return "forward:/index.html";
    }

}
