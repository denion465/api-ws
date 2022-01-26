package com.api.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.api.ws.exceptions.UserServiceException;
import com.api.ws.io.entity.UserEntity;
import com.api.ws.io.repositories.UserRepository;
import com.api.ws.service.UserService;
import com.api.ws.shared.Utils;
import com.api.ws.shared.dto.AddressDto;
import com.api.ws.shared.dto.UserDto;
import com.api.ws.ui.model.response.ErrorMessages;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    if (userRepository.findByEmail(user.getEmail()) != null)
      throw new RuntimeException("Record already exists");

    for (int i = 0; i < user.getAddresses().size(); i++) {
      AddressDto address = user.getAddresses().get(i);
      address.setUserDetails(user);
      address.setAddressId(utils.generateAddressId(30));
      user.getAddresses().set(i, address);
    }

    ModelMapper modelMapper = new ModelMapper();
    UserEntity userEntity = modelMapper.map(user, UserEntity.class);

    String publicUserId = utils.generateUserId(30);
    userEntity.setUserId(publicUserId);
    userEntity.setEncryptedPassword(bcryptPasswordEncoder.encode(user.getPassword()));

    UserEntity storedUserDetails = userRepository.save(userEntity);

    UserDto returnValue = modelMapper.map(storedUserDetails, UserDto.class);

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

  @Override
  public List<UserDto> getUsers(int page, int limit) {
    List<UserDto> returnValue = new ArrayList<>();
    Pageable pageableRequest = PageRequest.of(page, limit);

    Page<UserEntity> usersPage = userRepository.findAll(pageableRequest);
    List<UserEntity> users = usersPage.getContent();

    for (UserEntity userEntity : users) {
      UserDto userDto = new UserDto();
      BeanUtils.copyProperties(userEntity, userDto);
      returnValue.add(userDto);
    }

    return returnValue;
  }
}
