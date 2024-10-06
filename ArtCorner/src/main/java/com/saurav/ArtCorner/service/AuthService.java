package com.saurav.ArtCorner.service;

import com.saurav.ArtCorner.response.SignUpRequest;

public interface AuthService {

    String createUser(SignUpRequest req);
}
