package com.api.ws.ui.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.api.ws.service.AddressService;
import com.api.ws.service.UserService;
import com.api.ws.shared.Roles;
import com.api.ws.shared.dto.AddressDto;
import com.api.ws.shared.dto.UserDto;
import com.api.ws.ui.model.request.UserDetailsRequestModel;
import com.api.ws.ui.model.response.*;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
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

  @Autowired
  AddressService addressService;

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

  @PostAuthorize("hasRole('ADMIN') or returnObject.userId == principal.userId")
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

    ModelMapper modelMapper = new ModelMapper();
    UserDto userDto = modelMapper.map(userDetails, UserDto.class);
    userDto.setRoles(new HashSet<>(List.of(Roles.ROLE_USER.name())));

    UserDto createUser = userService.createUser(userDto);
    returnValue = modelMapper.map(createUser, UserRest.class);

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

  @PreAuthorize("hasRole('ROLE_ADMIN') or #id == principal.userId")
  //@PreAuthorize("hasAuthority('DELETE_AUTHORITY')")
  @DeleteMapping("/{id}")
  public OperationStatusModel deleteUser(@PathVariable String id) {
    OperationStatusModel returnValue = new OperationStatusModel();

    returnValue.setOperationName(RequestOperationName.DELETE.name());

    userService.deleteUser(id);

    returnValue.setOperationResult(RequestOperationStatus.SUCCESS.name());

    return returnValue;
  }

  @GetMapping("/{id}/addresses")
  public List<AddressesRest> getUserAddresses(@PathVariable String id) {
    List<AddressesRest> returnValue = new ArrayList<>();

    List<AddressDto> addressDto = addressService.getAddresses(id);

    if (addressDto != null && !addressDto.isEmpty()) {
      Type listType = new TypeToken<List<AddressesRest>>() {}.getType();

      returnValue = new ModelMapper().map(addressDto, listType);
    }

    return returnValue;
  }

  @GetMapping("/{id}/addresses/{addressId}")
  public AddressesRest getUserAddress(@PathVariable String addressId) {

    AddressDto addressDto = addressService.getAddress(addressId);

    ModelMapper modelMapper = new ModelMapper();

    return modelMapper.map(addressDto, AddressesRest.class);
  }
}
