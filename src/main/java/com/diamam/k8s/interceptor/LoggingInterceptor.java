package com.diamam.k8s.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("URI: {}, Method: {}", request.getRequestURI(), request.getMethod());
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("URI: {}, Method: {}, Status: {}", request.getRequestURI(), request.getMethod(), response.getStatus());
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

}
