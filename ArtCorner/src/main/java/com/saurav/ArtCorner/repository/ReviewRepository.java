package com.saurav.ArtCorner.repository;

import com.saurav.ArtCorner.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {

    List<Review> findByProductId(Long productId);
}
