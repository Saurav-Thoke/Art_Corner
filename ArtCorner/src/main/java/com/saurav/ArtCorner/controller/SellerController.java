package com.saurav.ArtCorner.controller;

import com.saurav.ArtCorner.config.JwtProvider;
import com.saurav.ArtCorner.model.Seller;
import com.saurav.ArtCorner.model.SellerReport;
import com.saurav.ArtCorner.model.VerificationCode;
import com.saurav.ArtCorner.repository.VerificationCodeRepository;
import com.saurav.ArtCorner.request.LoginRequest;
import com.saurav.ArtCorner.response.ApiResponse;
import com.saurav.ArtCorner.response.AuthResponse;
import com.saurav.ArtCorner.service.AuthService;
import com.saurav.ArtCorner.service.EmailService;
import com.saurav.ArtCorner.service.SellerReportService;
import com.saurav.ArtCorner.service.SellerService;
import com.saurav.ArtCorner.util.OtpUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller")
public class SellerController {
    private final SellerService sellerService;
    private final VerificationCodeRepository verificationCodeRepository;
    private final AuthService authService;
    private final EmailService emailService;
    private final SellerReportService sellerReportService;



    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginSeller(@RequestBody LoginRequest req) throws Exception {
        String otp=req.getOtp();
        String email= req.getEmail();


        req.setEmail("seller_"+email);
        AuthResponse authResponse=authService.signin(req);
        return ResponseEntity.ok(authResponse);
    }
    @PatchMapping("/verify/{otp}")

    public ResponseEntity<Seller> verifySellerEmail(@PathVariable String otp)throws Exception
    {
        VerificationCode verificationCode=verificationCodeRepository.findByOtp(otp);

        if(verificationCode==null || !verificationCode.getOtp().equals(otp))
        {
            throw new Exception("wrong otp");
        }

        Seller seller=sellerService.verifyEmail(verificationCode.getEmail(), otp);
        return new ResponseEntity<>(seller, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Seller> createSeller(@RequestBody Seller seller)throws Exception, MessagingException
    {
        Seller savedSeller=sellerService.createSeller(seller);

        String otp= OtpUtil.generateOtp();

        VerificationCode verificationCode=new VerificationCode();
        verificationCode.setOtp(otp);
        verificationCode.setEmail(seller.getEmail());
        verificationCodeRepository.save(verificationCode);

        String subject="Art Corner Email Verification code";
        String text="Welcome to the Art Corner,verify your account using this link";
        String frontend_url="http://localhost:8080/verify-seller/";
        emailService.sendVerificationOtpEmail(seller.getEmail(),verificationCode.getOtp(),subject,text+frontend_url);
        return new ResponseEntity<>(seller,HttpStatus.CREATED);

    }
    @GetMapping("/{id}")
    public ResponseEntity<Seller> getSellerById(@PathVariable Long id)throws Exception
    {
        Seller seller=sellerService.getSellerById(id);
        return new ResponseEntity<>(seller,HttpStatus.OK);
    }
    @GetMapping("/profile")
    public ResponseEntity<Seller> getSellerByJwt(@RequestHeader("Authorization") String jwt)throws Exception
    {
        Seller seller=sellerService.getSellerProfile(jwt);
        return new ResponseEntity<>(seller,HttpStatus.OK);
    }

    @PatchMapping()
    public ResponseEntity<Seller> updateSeller(@RequestHeader("Authorization") String jwt,@RequestBody Seller seller)throws Exception
    {
        Seller profile=sellerService.getSellerProfile(jwt);
        Seller updatedSeller=sellerService.updateSeller(profile.getId(), seller);
        return ResponseEntity.ok(updatedSeller);
    }

    @GetMapping
    public ResponseEntity<SellerReport> getSellersReport(
            @RequestHeader("Authentication")String jwt)throws Exception
    {
        Seller seller=sellerService.getSellerProfile(jwt);
        SellerReport report=sellerReportService.getSellerReport(seller);
        return new ResponseEntity<>(report,HttpStatus.OK);

    }
}
