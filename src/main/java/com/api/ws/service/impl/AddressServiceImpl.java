package com.api.ws.service.impl;

import com.api.ws.io.entity.AddressEntity;
import com.api.ws.io.entity.UserEntity;
import com.api.ws.io.repositories.AddressRepository;
import com.api.ws.io.repositories.UserRepository;
import com.api.ws.service.AddressService;
import com.api.ws.shared.dto.AddressDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

  @Autowired
  UserRepository userRepository;

  @Autowired
  AddressRepository addressRepository;

  @Override
  public List<AddressDto> getAddresses(String userId) {
    List<AddressDto> returnValue = new ArrayList<>();
    ModelMapper modelMapper = new ModelMapper();

    UserEntity userEntity = userRepository.findByUserId(userId);
    if (userEntity == null) return returnValue;

    Iterable<AddressEntity> address = addressRepository.findAllByUserDetails(userEntity);

    for (AddressEntity addressEntity : address) {
      returnValue.add(modelMapper.map(addressEntity, AddressDto.class));
    }

    return returnValue;
  }

  @Override
  public AddressDto getAddress(String adressId) {
    AddressDto returnValue = null;

    AddressEntity addressEntity = addressRepository.findByAddressId(adressId);

    if (addressEntity != null) {
      returnValue = new ModelMapper().map(addressEntity, AddressDto.class);
    }

    return returnValue;
  }
}
