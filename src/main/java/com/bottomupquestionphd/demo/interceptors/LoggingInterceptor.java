package com.bottomupquestionphd.demo.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

  private static final Logger log = LoggerFactory.getLogger(LoggingInterceptor.class);

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
          throws Exception {
    return HandlerInterceptor.super.preHandle((request), response, handler);
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                         ModelAndView modelAndView) throws Exception {
    HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                              Object handler, Exception ex) throws Exception {
    log.info("Method:[" + request.getMethod() + "]  URL:[" + request.getRequestURI() +
            "]  Status code:[" + response.getStatus() + "]");
    log.info("method: {} b {} c", request.getMethod(), request.getRequestURI());
    System.out.printf("%s %d hello", request.getRequestURI(), response.getStatus());
    HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
  }
}
