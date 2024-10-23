package com.saurav.ArtCorner.service.implementation;

import com.saurav.ArtCorner.model.Product;
import com.saurav.ArtCorner.model.Review;
import com.saurav.ArtCorner.model.User;
import com.saurav.ArtCorner.repository.ReviewRepository;
import com.saurav.ArtCorner.request.CreateReviewRequest;
import com.saurav.ArtCorner.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ExceptionDepthComparator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImplementaion implements ReviewService {

    private final ReviewRepository reviewRepository;
    @Override
    public Review createReview(CreateReviewRequest req, User user, Product product) {
        Review review=new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setReviewText(req.getReviewText());
        review.setRating(review.getRating());
        review.setProductImages(req.getProductImages());
        product.getReviews().add(review);
        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviewByProductId(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    @Override
    public Review updateReview(Long reviewId, String reviewText, int rating, Long userId) throws Exception {
        Review review=getReviewById(reviewId);
        if(review.getUser().getId().equals(userId))
        {
            review.setReviewText(reviewText);
            review.setRating(rating);
            return reviewRepository.save(review);
        }
        throw new Exception("You can not update this review");
    }

    @Override
    public void deleteReview(Long reviewId, Long userId) throws Exception {
            Review review=getReviewById(reviewId);
            if(review.getUser().getId().equals(userId))
            {
                throw new Exception("You can not delete this review");
            }
            reviewRepository.delete(review);
    }

    @Override
    public Review getReviewById(Long reviewId) throws Exception {
        return reviewRepository.findById(reviewId).orElseThrow(()->new Exception("Review not found"));
    }
}
