package com.saurav.ArtCorner.response;

import com.saurav.ArtCorner.domain.UserRole;
import lombok.Data;

@Data
public class AuthResponse {

    private String jwt;
    private String message;
    private UserRole role;
}
