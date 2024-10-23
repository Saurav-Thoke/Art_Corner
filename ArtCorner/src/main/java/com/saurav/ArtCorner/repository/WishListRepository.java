package com.saurav.ArtCorner.repository;

import com.saurav.ArtCorner.model.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListRepository extends JpaRepository<WishList,Long> {

    WishList findByUserId(Long userId);
}
