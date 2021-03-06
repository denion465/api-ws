package com.api.ws.security;

import com.api.ws.io.repositories.UserRepository;
import com.api.ws.service.UserService;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {
  private final UserService userService;
  private final BCryptPasswordEncoder bcryptPasswordEncoder;
  private final UserRepository userRepository;

  public WebSecurity(UserService userDetailsService, BCryptPasswordEncoder bcryptPasswordEncoder,
    UserRepository userRepository) {
    this.userService = userDetailsService;
    this.bcryptPasswordEncoder = bcryptPasswordEncoder;
    this.userRepository = userRepository;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable().authorizeRequests().antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL)
      .permitAll()
//      .antMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")
      .anyRequest()
      .authenticated()
      .and()
      .addFilter(getAuthenticationFilter())
      .addFilter(new AuthorizationFilter(authenticationManager(), userRepository))
      .sessionManagement()
      .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }

  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userService).passwordEncoder(bcryptPasswordEncoder);
  }

  public AuthenticationFilter getAuthenticationFilter() throws Exception {
    final AuthenticationFilter filter = new AuthenticationFilter(authenticationManager());
    filter.setFilterProcessesUrl("/users/login");

    return filter;
  }
}
