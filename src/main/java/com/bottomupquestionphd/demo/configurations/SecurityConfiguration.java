package com.bottomupquestionphd.demo.configurations;

import com.bottomupquestionphd.demo.repositories.AppUserRepository;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  private AppUserRepository appUserRepository;
  private AppUserPrincipalDetailsService appUserprincipalDetailsService;

  public SecurityConfiguration(AppUserRepository appUserRepository, AppUserPrincipalDetailsService appUserprincipalDetailsService) {
    this.appUserRepository = appUserRepository;
    this.appUserprincipalDetailsService = appUserprincipalDetailsService;
  }

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
  public HttpSessionEventPublisher httpSessionEventPublisher() {
    return new HttpSessionEventPublisher();
  }

  @Bean
  public PasswordEncoder getPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http
      .authorizeRequests().antMatchers("/register").permitAll()
      .and()
      .authorizeRequests()
      .anyRequest().authenticated()
      .and()
      .formLogin()
      .loginPage("/login").permitAll()
      .loginProcessingUrl("/login").permitAll()
      .failureUrl("/login-error")
      .defaultSuccessUrl("/landing-page")
      .and()
      .exceptionHandling()
      .authenticationEntryPoint(new EntryPoint())
      .and()
      .rememberMe()
      .alwaysRemember(true)
      .tokenValiditySeconds(30*5)
      .rememberMeCookieName("mouni")
      .key(System.getenv("SECRET_KEY"))
      .and()
      .csrf().disable();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return new AppUserPrincipalDetailsService(appUserRepository);
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

