package com.bottomupquestionphd.demo.configurations;

import com.bottomupquestionphd.demo.filters.JwtRequestFilter;
import com.bottomupquestionphd.demo.services.appuser.AppUserPrincipalDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  private AppUserPrincipalDetailsService appUserprincipalDetailsService;
  private JwtRequestFilter jwtRequestFilter;

  @Autowired
  public SecurityConfiguration(AppUserPrincipalDetailsService appUserprincipalDetailsService,
                               JwtRequestFilter jwtRequestFilter) {
    this.appUserprincipalDetailsService = appUserprincipalDetailsService;
    this.jwtRequestFilter = jwtRequestFilter;
  }

/*  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(appUserprincipalDetailsService);
  }*/

  @Autowired
  public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {

    auth.jdbcAuthentication().passwordEncoder(new BCryptPasswordEncoder());

  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(authenticationProvider());
  }

  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public PasswordEncoder getPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable()
      .authorizeRequests()
      .antMatchers("/register", "/login","/activation/**").permitAll()
      .antMatchers("/langing-page").authenticated()
      .antMatchers("/teacher/**").hasAnyRole("ADMIN", "TEACHER")
      .and().sessionManagement()
      .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
  }

  @Bean
  DaoAuthenticationProvider authenticationProvider(){
    DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider() ;
    daoAuthenticationProvider.setUserDetailsService(this.appUserprincipalDetailsService);
    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

    return daoAuthenticationProvider;
  }

  @Bean
  PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
  }
}

