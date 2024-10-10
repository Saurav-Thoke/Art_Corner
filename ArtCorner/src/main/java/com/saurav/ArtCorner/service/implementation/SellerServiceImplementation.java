package com.saurav.ArtCorner.service.implementation;

import com.saurav.ArtCorner.config.JwtProvider;
import com.saurav.ArtCorner.domain.UserRole;
import com.saurav.ArtCorner.model.Address;
import com.saurav.ArtCorner.model.Seller;
import com.saurav.ArtCorner.repository.AddressRepository;
import com.saurav.ArtCorner.repository.SellerRepository;
import com.saurav.ArtCorner.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerServiceImplementation implements SellerService {

    private final SellerRepository sellerRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final AddressRepository addressRepository;
    @Override
    public Seller getSellerProfile(String jwt) throws Exception {
        String email=jwtProvider.getEmailFromJwtToken(jwt);
        return this.getSellerByEmail(email);
    }

    @Override
    public Seller createSeller(Seller seller) throws Exception {
        Seller sellerExist=sellerRepository.findByEmail(seller.getEmail());
        if(sellerExist!=null)
        {
            throw new Exception("Seller already exists with this email id ");
        }
        Address savedAddress=addressRepository.save(seller.getPickupAddress());

        Seller newSeller=new Seller();
        newSeller.setEmail(seller.getEmail());
        newSeller.setPassword(passwordEncoder.encode(seller.getPassword()));
        newSeller.setSellerName(seller.getSellerName());
        newSeller.setPickupAddress(savedAddress);
        newSeller.setGSTIN(seller.getGSTIN());
        newSeller.setRole(UserRole.ROLE_ADMIN);
        newSeller.setMobile(seller.getMobile());
        newSeller.setBankDetails(seller.getBankDetails());
        newSeller.setBusinessDetails(seller.getBusinessDetails());
        return sellerRepository.save(newSeller);
    }

    @Override
    public Seller getSellerById(Long id) throws Exception {
        return sellerRepository.findById(id).orElseThrow(()->new Exception("Seller not found"+id));
    }

    @Override
    public Seller getSellerByEmail(String email) throws Exception {
        Seller seller=sellerRepository.findByEmail(email);
        if(seller==null)
                throw new Exception("Seller not found");
        return seller;
    }

    @Override
    public Seller updateSeller(Long id, Seller seller) throws Exception {
        Seller existingSeller =this.getSellerById(id);

        if(seller.getSellerName()!=null)
        {
            existingSeller.setSellerName(seller.getSellerName());
        }
        if(seller.getMobile()!=null)
        {
            existingSeller.setMobile(seller.getMobile());
        }
        if(seller.getEmail()!=null)
        {
            existingSeller.setEmail(seller.getEmail());
        }
        if(seller.getBusinessDetails()!=null && seller.getBusinessDetails().getBusinessName()!=null)
        {
            existingSeller.getBusinessDetails().setBusinessName((seller.getBusinessDetails().getBusinessName()));
        }
        if(seller.getBankDetails()!=null
                && seller.getBankDetails().getAccountHolderName()!=null
                && seller.getBankDetails().getAccountNumber()!=null
                && seller.getBankDetails().getIfscCode()!=null
                )
        {
            existingSeller.getBankDetails().setAccountHolderName((seller.getBankDetails().getAccountHolderName()));
            existingSeller.getBankDetails().setAccountNumber(seller.getBankDetails().getAccountNumber());
            existingSeller.getBankDetails().setIfscCode(seller.getBankDetails().getIfscCode());
        }

        if(seller.getPickupAddress()!=null
                && seller.getPickupAddress().getAddress()!=null
                &&  seller.getPickupAddress().getMobile()!=null
                && seller.getPickupAddress().getCity()!=null
                && seller.getPickupAddress().getState()!=null)
        {
            existingSeller.getPickupAddress().setAddress(seller.getPickupAddress().getAddress());
            existingSeller.getPickupAddress().setCity(seller.getPickupAddress().getCity());
            existingSeller.getPickupAddress().setState(seller.getPickupAddress().getState());
            existingSeller.getPickupAddress().setMobile(seller.getPickupAddress().getMobile());
            existingSeller.getPickupAddress().setPinCode(seller.getPickupAddress().getPinCode());
        }
        if(seller.getGSTIN()!=null)
        {
            existingSeller.setGSTIN(seller.getGSTIN());
        }
        return sellerRepository.save(existingSeller);
    }

    @Override
    public Seller verifyEmail(String email, String otp) throws Exception {
        Seller seller=getSellerByEmail(email);
        seller.setEmailVerified(true);
        return sellerRepository.save(seller);
    }
}
