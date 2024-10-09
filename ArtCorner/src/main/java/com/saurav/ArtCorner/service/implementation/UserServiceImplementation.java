package com.saurav.ArtCorner.service.implementation;

import com.saurav.ArtCorner.config.JwtProvider;
import com.saurav.ArtCorner.model.User;
import com.saurav.ArtCorner.repository.UserRepository;
import com.saurav.ArtCorner.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Override
    public User findUserByJwtToken(String jwt) throws Exception {
        String email=jwtProvider.getEmailFromJwtToken(jwt);
        return this.findUserByEmail(email);

    }

    @Override
    public User findUserByEmail(String email) throws Exception {

        User user=userRepository.findByEmail(email);
        if(user==null)
        {
            throw new Exception("User not found with email - "+email);
        }
        return userRepository.findByEmail(email);

    }
}
