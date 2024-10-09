package com.saurav.ArtCorner.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String otp;
}
