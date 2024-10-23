package com.saurav.ArtCorner.repository;

import com.saurav.ArtCorner.model.Deal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DealRepository extends JpaRepository<Deal,Long> {
}
