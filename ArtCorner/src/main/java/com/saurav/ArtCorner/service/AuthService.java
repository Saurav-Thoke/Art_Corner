package com.saurav.ArtCorner.service;

import com.saurav.ArtCorner.domain.UserRole;
import com.saurav.ArtCorner.request.LoginRequest;
import com.saurav.ArtCorner.response.AuthResponse;
import com.saurav.ArtCorner.response.SignUpRequest;

public interface AuthService {



    String createUser(SignUpRequest req) throws Exception;

    void sendLoginOtp(String email, UserRole role) throws Exception;

    AuthResponse signin(LoginRequest req);
}
