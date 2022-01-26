package com.api.ws.service;

import com.api.ws.shared.dto.AddressDto;

import java.util.List;

public interface AddressService {
  List<AddressDto> getAddresses(String userId);
  AddressDto getAddress(String adressId);
}
