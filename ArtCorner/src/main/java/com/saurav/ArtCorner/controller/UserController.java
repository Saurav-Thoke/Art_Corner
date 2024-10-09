package com.saurav.ArtCorner.controller;

import com.saurav.ArtCorner.domain.UserRole;
import com.saurav.ArtCorner.model.User;
import com.saurav.ArtCorner.response.AuthResponse;
import com.saurav.ArtCorner.response.SignUpRequest;
import com.saurav.ArtCorner.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/profile")
    public ResponseEntity<User> createUserHandler(@RequestHeader("Authorization") String jwt) throws Exception {

        User user=userService.findUserByJwtToken(jwt);
        return ResponseEntity.ok(user);
    }



}
