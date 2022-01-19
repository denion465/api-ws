package com.api.ws.io.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity(name = "users")
public class UserEntity implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue
  private long id;

  @Column(nullable = false)
  private String userId;

  @Column(nullable = false, length = 50)
  private String firstName;

  @Column(nullable = false, length = 50)
  private String lastName;

  @Column(nullable = false, length = 120)
  private String email;

  @Column(nullable = false)
  private String encryptedPassword;

  private String emailVerificationToken;

  @Column(nullable = false)
  private Boolean emailVerificationStatus = false;

  @OneToMany(mappedBy = "userDetails", cascade = CascadeType.ALL)
  private List<AddressEntity> addresses;

  public long getId() {
    return this.id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getUserId() {
    return this.userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getFirstName() {
    return this.firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return this.lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getEncryptedPassword() {
    return this.encryptedPassword;
  }

  public void setEncryptedPassword(String encryptedPassword) {
    this.encryptedPassword = encryptedPassword;
  }

  public String getEmailVerificationToken() {
    return this.emailVerificationToken;
  }

  public void setEmailVerificationToken(String emailVerificationToken) {
    this.emailVerificationToken = emailVerificationToken;
  }

  public Boolean isEmailVerificationStatus() {
    return this.emailVerificationStatus;
  }

  public Boolean getEmailVerificationStatus() {
    return this.emailVerificationStatus;
  }

  public void setEmailVerificationStatus(Boolean emailVerificationStatus) {
    this.emailVerificationStatus = emailVerificationStatus;
  }
}
