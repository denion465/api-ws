package com.api.ws.ui.controller;

import java.util.ArrayList;
import java.util.List;

import com.api.ws.service.UserService;
import com.api.ws.shared.dto.UserDto;
import com.api.ws.ui.model.request.UserDetailsRequestModel;
import com.api.ws.ui.model.response.OperationStatusModel;
import com.api.ws.ui.model.response.RequestOperationName;
import com.api.ws.ui.model.response.RequestOperationStatus;
import com.api.ws.ui.model.response.UserRest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("users")
public class UserController {
  @Autowired
  UserService userService;

  @GetMapping
  public List<UserRest> getUsers(
    @RequestParam(value = "page", defaultValue = "0") int page,
    @RequestParam(value = "limit", defaultValue = "25") int limit
  ) {
      List<UserRest> returnValue = new ArrayList<>();

      List<UserDto> users = userService.getUsers(page, limit);

      for (UserDto userDto : users) {
        UserRest userModel = new UserRest();
        BeanUtils.copyProperties(userDto, userModel);
        returnValue.add(userModel);
      }

      return returnValue;
  }

  @GetMapping("/{id}")
  public UserRest getUser(@PathVariable String id) {
    UserRest returnValue = new UserRest();

    UserDto userDto = userService.getUserByUserId(id);
    BeanUtils.copyProperties(userDto, returnValue);

    return returnValue;
  }

  @PostMapping
  public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) throws Exception {
    UserRest returnValue = new UserRest();

    // UserDto userDto = new UserDto();
    // BeanUtils.copyProperties(userDetails, userDto);
    ModelMapper modelMapper = new ModelMapper();
    UserDto userDto = modelMapper.map(userDetails, UserDto.class);

    UserDto createUser = userService.createUser(userDto);
    BeanUtils.copyProperties(createUser, returnValue);

    return returnValue;
  }

  @PutMapping("/{id}")
  public UserRest updateUser(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {
    UserRest returnValue = new UserRest();

    UserDto userDto = new UserDto();
    BeanUtils.copyProperties(userDetails, userDto);

    UserDto updateUser = userService.updateUser(id, userDto);
    BeanUtils.copyProperties(updateUser, returnValue);

    return returnValue;
  }

  @DeleteMapping("/{id}")
  public OperationStatusModel deleteUser(@PathVariable String id) {
    OperationStatusModel returnValue = new OperationStatusModel();

    returnValue.setOperationName(RequestOperationName.DELETE.name());

    userService.deleteUser(id);

    returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());

    return returnValue;
  }
}
