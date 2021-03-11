package com.rodtech.qideasauthapi.service;

import com.rodtech.qideasauthapi.dto.UserDTO;

public interface UserService {

    UserDTO create(UserDTO userDTO);

    UserDTO findByEmail(String email);

}
