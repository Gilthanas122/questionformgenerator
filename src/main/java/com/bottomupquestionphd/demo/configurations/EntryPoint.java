package com.bottomupquestionphd.demo.configurations;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EntryPoint implements AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
                       AuthenticationException authException) throws IOException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.getWriter().write("Username or password invalid");
  }

}
