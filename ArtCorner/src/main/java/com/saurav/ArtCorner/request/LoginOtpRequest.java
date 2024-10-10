package com.saurav.ArtCorner.request;

import com.saurav.ArtCorner.domain.UserRole;
import lombok.Data;

@Data
public class LoginOtpRequest {
    private String email;
    private String otp;
    private UserRole role;
}
