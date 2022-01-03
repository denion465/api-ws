package com.api.ws;

import com.api.ws.security.AppProperties;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class AppWsApplication {
	public static void main(String[] args) {
		SpringApplication.run(AppWsApplication.class, args);
	}

  @Bean
  public BCryptPasswordEncoder bcryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SpringApplicationContext springApplicationContext() {
    return  new SpringApplicationContext();
  }

  @Bean(name="AppProperties")
  public AppProperties getAppProperties() {
    return new AppProperties();
  }
}
