package com.saurav.ArtCorner.controller;

import com.saurav.ArtCorner.domain.UserRole;
import com.saurav.ArtCorner.model.User;
import com.saurav.ArtCorner.repository.UserRepository;
import com.saurav.ArtCorner.response.AuthResponse;
import com.saurav.ArtCorner.response.SignUpRequest;
import com.saurav.ArtCorner.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final AuthService authService;
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignUpRequest req)
    {
        String jwt=authService.createUser(req);

        AuthResponse res=new AuthResponse();
        res.setJwt(jwt);
        res.setMessage("registered successfully");
        res.setRole(UserRole.ROLE_CUSTOMER);
        return ResponseEntity.ok(res);
    }
}