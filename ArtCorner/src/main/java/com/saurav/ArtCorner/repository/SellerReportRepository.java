package com.saurav.ArtCorner.repository;

import com.saurav.ArtCorner.model.SellerReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerReportRepository extends JpaRepository<SellerReport,Long> {

    SellerReport findBySellerId(Long sellerId);
}
