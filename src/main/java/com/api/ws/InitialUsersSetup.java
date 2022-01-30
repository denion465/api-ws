package com.api.ws;

import com.api.ws.io.entity.AuthorityEntity;
import com.api.ws.io.entity.RoleEntity;
import com.api.ws.io.entity.UserEntity;
import com.api.ws.io.repositories.AuthorityRepository;
import com.api.ws.io.repositories.RoleRepository;
import com.api.ws.io.repositories.UserRepository;
import com.api.ws.shared.Roles;
import com.api.ws.shared.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
public class InitialUsersSetup {

  @Autowired
  AuthorityRepository authorityRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  Utils utils;

  @Autowired
  BCryptPasswordEncoder bCryptPasswordEncoder;

  @EventListener
  @Transactional
  public void onApplicationEvent(ApplicationReadyEvent event) {
    AuthorityEntity readAuthority = createAuthority("READ_AUTHORITY");
    AuthorityEntity writeAuthority = createAuthority("WRITE_AUTHORITY");
    AuthorityEntity deleteAuthority = createAuthority("DELETE_AUTHORITY");

    createRole(Roles.ROLE_USER.name(), Arrays.asList(readAuthority, writeAuthority));
    RoleEntity roleAdmin = createRole(Roles.ROLE_ADMIN.name(), Arrays.asList(readAuthority, writeAuthority, deleteAuthority));

    if (roleAdmin == null) return;

    UserEntity adminUser = new UserEntity();
    adminUser.setFirstName("Daniel");
    adminUser.setLastName("Vidal");
    adminUser.setEmail("danAdmin@hot.com");
    adminUser.setEmailVerificationStatus(true);
    adminUser.setUserId(utils.generateUserId(30));
    adminUser.setEncryptedPassword(bCryptPasswordEncoder.encode("123456"));
    adminUser.setRoles(List.of(roleAdmin));

    UserEntity storedUserDetails = userRepository.findByEmail("danAdmin@hot.com");
    if (storedUserDetails == null) {
      userRepository.save(adminUser);
    }
  }

  @Transactional
  private AuthorityEntity createAuthority(String name) {
    AuthorityEntity authority = authorityRepository.findByName(name);

    if (authority == null) {
      authority = new AuthorityEntity(name);
      authorityRepository.save(authority);
    }

    return authority;
  }

  @Transactional
  private RoleEntity createRole(String name, Collection<AuthorityEntity> authorities) {
    RoleEntity role = roleRepository.findByName(name);

    if (role == null) {
      role = new RoleEntity(name);
      role.setAuthorities(authorities);
      roleRepository.save(role);
    }

    return role;
  }
}
