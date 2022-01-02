package com.appsdeveloperblog.app.ws.service.impl;

import com.appsdeveloperblog.app.ws.UserRepository;
import com.appsdeveloperblog.app.ws.io.entity.UserEntity;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.Utils;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
  @Autowired
  UserRepository userRepository;

  @Autowired
  Utils utils;

  @Override
  public UserDto createUser(UserDto user) {
    UserEntity storedUserDetails = userRepository.findByEmail(user.getEmail());

    if (storedUserDetails != null) throw new RuntimeException("Record already exists");

    UserEntity userEntity = new UserEntity();
    BeanUtils.copyProperties(user, userEntity);

    String publicUserId = utils.generateUserId(30);
    userEntity.setUserId(publicUserId);
    userEntity.setEncryptedPassword("test");

    UserEntity storedUserDetail = userRepository.save(userEntity);

    UserDto returnValue = new UserDto();
    BeanUtils.copyProperties(storedUserDetail, returnValue);

    return returnValue;
  }
}