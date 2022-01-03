package com.api.ws.service;

import com.api.ws.shared.dto.UserDto;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
  UserDto createUser(UserDto user);
  UserDto getUser(String email);
}