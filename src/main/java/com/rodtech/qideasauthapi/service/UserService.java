package com.rodtech.qideasauthapi.service;

import com.rodtech.qideasauthapi.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    Page<UserDTO> list(Pageable pageable);

    UserDTO create(UserDTO userDTO);

    UserDTO findByEmail(String email);

    void deleteByEmail(String id);

}
