package com.saurav.ArtCorner.service;

import com.saurav.ArtCorner.model.Seller;
import com.saurav.ArtCorner.model.SellerReport;

public interface SellerReportService {

    SellerReport getSellerReport(Seller seller);
    SellerReport updateSellerReport(SellerReport sellerReport);
}
