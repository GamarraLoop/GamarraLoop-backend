package com.gamarraloop.platform.shared.infrastructure.config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class HttpMethodOverrideFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String overrideMethod = httpRequest.getHeader("X-HTTP-Method-Override");

        if (overrideMethod != null && !overrideMethod.isEmpty() && httpRequest.getMethod().equalsIgnoreCase("POST")) {
            HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(httpRequest) {
                @Override
                public String getMethod() {
                    return overrideMethod.toUpperCase();
                }
            };
            chain.doFilter(requestWrapper, response);
        } else {
            chain.doFilter(request, response);
        }
    }
}
