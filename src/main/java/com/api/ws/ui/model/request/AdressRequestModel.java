package com.api.ws.ui.model.request;

public class AdressRequestModel {
  private String city;
  private String country;
  private String streetName;
  private String postalCode;
  private String type;

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
}
