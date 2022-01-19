package com.api.ws.io.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.api.ws.shared.dto.UserDto;

@Entity(name = "addresses")
public class AddressEntity implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  private long id;

  @Column(length = 30, nullable = false)
  private String addressId;

  @Column(length = 15, nullable = false)
  private String city;

  @Column(length = 15, nullable = false)
  private String country;

  @Column(length = 100, nullable = false)
  private String streetName;

  @Column(length = 7, nullable = false)
  private String postalCode;

  @Column(length = 10, nullable = false)
  private String type;

  @ManyToOne
  @JoinColumn(name = "users_id")
  private UserDto userDetails;

  public long getId() {
    return this.id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getAddressId() {
    return this.addressId;
  }

  public void setAddressId(String addressId) {
    this.addressId = addressId;
  }

  public String getCity() {
    return this.city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getCountry() {
    return this.country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public String getStreetName() {
    return this.streetName;
  }

  public void setStreetName(String streetName) {
    this.streetName = streetName;
  }

  public String getPostalCode() {
    return this.postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public String getType() {
    return this.type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public UserDto getUserDetails() {
    return this.userDetails;
  }

  public void setUserDetails(UserDto userDetails) {
    this.userDetails = userDetails;
  }
}
