package com.saurav.ArtCorner.service;

import com.saurav.ArtCorner.model.User;

public interface UserService {

    User findUserByJwtToken(String jwt) throws Exception;
    User findUserByEmail(String email) throws Exception;
}
