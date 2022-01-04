package com.api.ws.service.impl;

import java.util.ArrayList;

import com.api.ws.exceptions.UserServiceException;
import com.api.ws.io.entity.UserEntity;
import com.api.ws.io.repositories.UserRepository;
import com.api.ws.service.UserService;
import com.api.ws.shared.Utils;
import com.api.ws.shared.dto.UserDto;
import com.api.ws.ui.model.response.ErrorMessages;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
  @Autowired
  UserRepository userRepository;

  @Autowired
  Utils utils;

  @Autowired
  BCryptPasswordEncoder bcryptPasswordEncoder;

  @Override
  public UserDto createUser(UserDto user) {
    UserEntity storedUserDetails = userRepository.findByEmail(user.getEmail());

    if (storedUserDetails != null) throw new RuntimeException("Record already exists");

    UserEntity userEntity = new UserEntity();
    BeanUtils.copyProperties(user, userEntity);

    String publicUserId = utils.generateUserId(30);
    userEntity.setUserId(publicUserId);
    userEntity.setEncryptedPassword(bcryptPasswordEncoder.encode(user.getPassword()));

    UserEntity storedUserDetail = userRepository.save(userEntity);

    UserDto returnValue = new UserDto();
    BeanUtils.copyProperties(storedUserDetail, returnValue);

    return returnValue;
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    UserEntity userEntity = userRepository.findByEmail(email);

    if (userEntity == null) throw new UsernameNotFoundException(email);

    return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
  }

  @Override
  public UserDto getUser(String email) {
    UserEntity userEntity = userRepository.findByEmail(email);

    if (userEntity == null) throw new UsernameNotFoundException(email);

    UserDto returnValue = new UserDto();
    BeanUtils.copyProperties(userEntity, returnValue);

    return returnValue;
  }

  @Override
  public UserDto getUserByUserId(String userId) {
    UserDto returnValue = new UserDto();
    UserEntity userEntity = userRepository.findByUserId(userId);

    if (userEntity == null)
      throw new UsernameNotFoundException("User with ID: " + userId + " not found");

    BeanUtils.copyProperties(userEntity, returnValue);

    return returnValue;
  }

  @Override
  public UserDto updateUser(String userId, UserDto user) {
    UserDto returnValue = new UserDto();
    UserEntity userEntity = userRepository.findByUserId(userId);

    if (userEntity == null)
      throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

    userEntity.setFirstName(user.getFirstName());
    userEntity.setLastName(user.getLastName());

    UserEntity updatedUserDetails = userRepository.save(userEntity);

    BeanUtils.copyProperties(updatedUserDetails, returnValue);

    return returnValue;
  }

  @Override
  public void deleteUser(String userId) {
    UserEntity userEntity = userRepository.findByUserId(userId);

    if (userEntity == null)
      throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

    userRepository.delete(userEntity);
  }
}
