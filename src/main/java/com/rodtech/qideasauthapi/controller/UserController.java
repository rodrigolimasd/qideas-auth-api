package com.rodtech.qideasauthapi.controller;

import com.rodtech.qideasauthapi.dto.UserDTO;
import com.rodtech.qideasauthapi.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Log4j2
@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("#oauth2.hasScope('write')")
    @GetMapping
    public ResponseEntity<?> list(Pageable pageable){
        log.info("listing users {} ", pageable);
        Page<UserDTO> list = userService.list(pageable);
        log.info("listed users: size {} ", list.getSize());
        return ResponseEntity.ok(list);
    }

    @PreAuthorize("#oauth2.hasScope('write')")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody UserDTO userDTO) {
        log.info("creating user {} ", userDTO);
        UserDTO newUSer = userService.create(userDTO);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/email/{email}")
                .buildAndExpand(newUSer.getEmail())
                .toUri();
        log.info("user created {} ", userDTO);
        return ResponseEntity.created(uri).body(newUSer);
    }

    @PreAuthorize("#oauth2.hasScope('write')")
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getByEmail(@PathVariable String email) {
        log.info("finding user by email {} ", email);
        UserDTO user = userService.findByEmail(email);
        log.info("user find {} ", user);
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("#oauth2.hasScope('write')")
    @DeleteMapping("/email/{email}")
    public ResponseEntity<?> delete(@PathVariable String email){
        log.info("deleting user by email {} ", email);
        userService.deleteByEmail(email);
        log.info("deleted user by email {} ", email);
        return ResponseEntity.noContent().build();
    }
}
