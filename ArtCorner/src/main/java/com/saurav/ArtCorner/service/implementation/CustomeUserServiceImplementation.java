package com.saurav.ArtCorner.service.implementation;

import com.saurav.ArtCorner.domain.UserRole;
import com.saurav.ArtCorner.model.Seller;
import com.saurav.ArtCorner.model.User;
import com.saurav.ArtCorner.repository.SellerRepository;
import com.saurav.ArtCorner.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CustomeUserServiceImplementation implements UserDetailsService {

    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    private static final String SELLER_PREFIX="seller_";
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username.startsWith(SELLER_PREFIX))
        {
            String actualUserName=username.substring(SELLER_PREFIX.length());
            Seller seller=sellerRepository.findByEmail(actualUserName);//-------------------------//-------------------//
            if(seller!=null)
            {
                return buildUserDetails(seller.getEmail(),seller.getPassword(),seller.getRole());
            }
        }
        else
        {
            User user=userRepository.findByEmail(username);
            if(user!=null)
            {
                return buildUserDetails(user.getEmail(),user.getPassword(),user.getRole());
            }
        }
        throw new UsernameNotFoundException("user not found with email - "+username);
    }

    private UserDetails buildUserDetails(String email, String password, UserRole role) {

        if(role==null)
                role=UserRole.ROLE_CUSTOMER;

        List<GrantedAuthority> authorityList=new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority(role.toString()));

        return new org.springframework.security.core.userdetails.User(email,password,authorityList);
    }
}
