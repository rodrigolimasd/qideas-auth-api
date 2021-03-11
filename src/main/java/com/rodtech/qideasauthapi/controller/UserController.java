package com.rodtech.qideasauthapi.controller;

import com.rodtech.qideasauthapi.dto.UserDTO;
import com.rodtech.qideasauthapi.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("#oauth2.hasScope('write')")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody UserDTO userDTO) {
        UserDTO newUSer = userService.create(userDTO);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/email/{email}")
                .buildAndExpand(newUSer.getEmail())
                .toUri();

        return ResponseEntity.created(uri).body(newUSer);
    }

    @PreAuthorize("#oauth2.hasScope('write') OR #oauth2.hasScope('read')")
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }
}
