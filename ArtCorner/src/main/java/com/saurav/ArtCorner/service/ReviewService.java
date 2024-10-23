package com.saurav.ArtCorner.service;

import com.saurav.ArtCorner.model.Product;
import com.saurav.ArtCorner.model.Review;
import com.saurav.ArtCorner.model.User;
import com.saurav.ArtCorner.request.CreateReviewRequest;

import java.util.List;

public interface ReviewService {

    Review createReview(CreateReviewRequest req, User user, Product product);
    List<Review> getReviewByProductId(Long productId);

    Review updateReview(Long reviewId,String reviewText,int rating,Long userId) throws Exception;

    void deleteReview(Long reviewId,Long userId) throws Exception;
    Review getReviewById(Long reviewId) throws Exception;

}
