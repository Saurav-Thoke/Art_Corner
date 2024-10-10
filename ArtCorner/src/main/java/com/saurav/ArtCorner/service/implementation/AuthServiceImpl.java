package com.saurav.ArtCorner.service.implementation;

import com.saurav.ArtCorner.config.JwtProvider;
import com.saurav.ArtCorner.domain.UserRole;
import com.saurav.ArtCorner.model.Cart;
import com.saurav.ArtCorner.model.Seller;
import com.saurav.ArtCorner.model.User;
import com.saurav.ArtCorner.model.VerificationCode;
import com.saurav.ArtCorner.repository.CartRepository;
import com.saurav.ArtCorner.repository.SellerRepository;
import com.saurav.ArtCorner.repository.UserRepository;
import com.saurav.ArtCorner.repository.VerificationCodeRepository;
import com.saurav.ArtCorner.request.LoginRequest;
import com.saurav.ArtCorner.response.AuthResponse;
import com.saurav.ArtCorner.response.SignUpRequest;
import com.saurav.ArtCorner.service.AuthService;
import com.saurav.ArtCorner.service.EmailService;
import com.saurav.ArtCorner.util.OtpUtil;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.prefs.BackingStoreException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;
    private final JwtProvider jwtProvider;
    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailService emailService;
    private final CustomeUserServiceImplementation customeUserService;
    private final SellerRepository sellerRepository;
    @Override
    public String createUser(SignUpRequest req) throws Exception {

        VerificationCode verificationCode=verificationCodeRepository.findByEmail(req.getEmail());

        if(verificationCode==null || !verificationCode.getOtp().equals(req.getOtp()))
        {
            throw new Exception("Wrong otp");
        }

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

    @Override
    public void sendLoginOtp(String email,UserRole role) throws Exception {
        String SIGNIN_PREFIX = "signin_";
        if(email.startsWith(SIGNIN_PREFIX))
        {
            email=email.substring(SIGNIN_PREFIX.length());

            if(role.equals(UserRole.ROLE_ADMIN))
            {
                Seller seller=sellerRepository.findByEmail(email);
                if(seller==null)
                {
                    throw new Exception("Seller not found");
                }

            }
            else
            {
                User user=userRepository.findByEmail(email);
                if(user==null) {
                    throw new Exception("User not exist whith this email");
                }
            }
        }

        VerificationCode isExist=verificationCodeRepository.findByEmail(email);
        if(isExist!=null)
        {
            verificationCodeRepository.delete(isExist);
        }

        String otp= OtpUtil.generateOtp();

        VerificationCode verificationCode=new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(email);
        verificationCodeRepository.save(verificationCode);

        String subject="Art Corner login/signup otp";
        String text="login/signup otp is - "+otp;
        emailService.sendVerificationOtpEmail(email,otp,subject,text);
    }

    @Override
    public AuthResponse signin(LoginRequest req) {
        String userName=req.getEmail();
        String otp= req.getOtp();

        Authentication authentication = authenticate(userName,otp);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token=jwtProvider.generateToken(authentication);

        AuthResponse authResponse=new AuthResponse();
        authResponse.setJwt(token);
        authResponse.setMessage("Login sucessfull");

        Collection<? extends GrantedAuthority> authorities=authentication.getAuthorities();
        String roleName=authorities.isEmpty()?null:authorities.iterator().next().getAuthority();

        authResponse.setRole((UserRole.valueOf(roleName)));
        return authResponse;
    }

    private Authentication authenticate(String userName, String otp) {

        UserDetails userDetails=customeUserService.loadUserByUsername(userName);

        String SELLER_PREFIX="seller_";
        if(userName.startsWith(SELLER_PREFIX))
        {
            userName=userName.substring(SELLER_PREFIX.length());
        }
        if(userDetails==null)
        {
            throw new BadCredentialsException("Invalid username or password");
        }

        VerificationCode verificationCode=verificationCodeRepository.findByEmail(userName);
        if(verificationCode==null|| !verificationCode.getOtp().equals(otp))
        {
            throw new BadCredentialsException("Wrong otp");
        }
        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }
}
