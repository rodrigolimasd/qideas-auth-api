package com.rodtech.qideasauthapi.dto;

import com.rodtech.qideasauthapi.model.BaseModelAPI;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO extends BaseModelAPI {
    private String email;
    private String username;
    private String password;
}
