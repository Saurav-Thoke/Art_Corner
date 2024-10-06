package com.saurav.ArtCorner.service.implementation;

import com.saurav.ArtCorner.config.JwtProvider;
import com.saurav.ArtCorner.domain.UserRole;
import com.saurav.ArtCorner.model.Cart;
import com.saurav.ArtCorner.model.User;
import com.saurav.ArtCorner.repository.CartRepository;
import com.saurav.ArtCorner.repository.UserRepository;
import com.saurav.ArtCorner.response.SignUpRequest;
import com.saurav.ArtCorner.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final JwtProvider jwtProvider;
    @Override
    public String createUser(SignUpRequest req) {

        User user=userRepository.findByEmail(req.getEmail());
        if(user==null)
        {
            User createdUser=new User();
            createdUser.setEmail(req.getEmail());
            createdUser.setFullName(req.getFullName());

            createdUser.setRole(UserRole.ROLE_CUSTOMER);
            createdUser.setMobile("7879797979");
            createdUser.setPassword(passwordEncoder.encode(req.getOtp()));
            user=userRepository.save(createdUser);
            Cart cart=new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }

        List<GrantedAuthority> authorities=new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(UserRole.ROLE_CUSTOMER.toString()));
        Authentication authentication=new UsernamePasswordAuthenticationToken(req.getEmail(),null,authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return jwtProvider.generateToken(authentication);
    }
}
