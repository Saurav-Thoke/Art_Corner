package com.saurav.ArtCorner.service;

import com.saurav.ArtCorner.model.Seller;

import java.util.List;

public interface SellerService {

    Seller getSellerProfile(String jwt) throws Exception;
    Seller createSeller(Seller seller) throws Exception;
    Seller getSellerById(Long id) throws Exception;
    Seller getSellerByEmail(String email) throws Exception;
    Seller updateSeller(Long id,Seller seller) throws Exception;
    Seller verifyEmail(String email,String otp) throws Exception;

}
