package com.rodtech.qideasauthapi.service.impl;

import com.rodtech.qideasauthapi.dto.UserDTO;
import com.rodtech.qideasauthapi.enums.UserType;
import com.rodtech.qideasauthapi.exception.RegisteredEmailException;
import com.rodtech.qideasauthapi.exception.UserNotFoundException;
import com.rodtech.qideasauthapi.model.Permission;
import com.rodtech.qideasauthapi.model.User;
import com.rodtech.qideasauthapi.repository.UserRepository;
import com.rodtech.qideasauthapi.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service("userDetailsService")
public class UserServiceImpl implements UserService, UserDetailsService {

    private static final String EMAIL_REGISTERED = "E-mail already registered";

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findFirstByEmail(email)
                .orElseThrow(()-> new BadCredentialsException("Bad Credentials"));
        return user;
    }

    @Override
    public Page<UserDTO> list(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(p-> UserDTO.builder()
                        .id(p.getId())
                        .email(p.getEmail())
                        .build());
    }

    @Override
    public UserDTO create(UserDTO userDTO) {

        //get dto
        User user = User.builder()
                .email(userDTO.getEmail())
                .password("{bcrypt}" + BCrypt.hashpw(userDTO.getPassword(), BCrypt.gensalt(10)))
                .enabled(true)
                .type(UserType.USER)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .credentialsNonExpired(true)
                .permissions(Collections.singleton(
                        Permission.builder().name("client").build()
                ))
                .build();

        //valid
        validSaveUser(user);

        user = userRepository.save(user);
        userDTO.setId(user.getId());

        return userDTO;
    }

    @Override
    public UserDTO findByEmail(String email) {
        User user = findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return UserDTO.builder()
                .email(user.getEmail())
                .build();
    }

    @Override
    public void deleteByEmail(String email) {
        User user = userRepository.findFirstByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        userRepository.delete(user);
    }

    private void validSaveUser(User user){
        findUserByEmail(user.getEmail())
                .ifPresent(userDb -> {
                    if(user.getId()==null || !user.getId().equals(userDb.getId()))
                        throw new RegisteredEmailException(EMAIL_REGISTERED);
                });
    }

    private Optional<User> findUserByEmail(String email){
        return userRepository.findFirstByEmail(email);
    }

}
