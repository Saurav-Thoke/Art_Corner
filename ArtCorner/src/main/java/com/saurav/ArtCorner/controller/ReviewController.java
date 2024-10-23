package com.saurav.ArtCorner.controller;

import com.saurav.ArtCorner.model.Product;
import com.saurav.ArtCorner.model.Review;
import com.saurav.ArtCorner.model.User;
import com.saurav.ArtCorner.request.CreateReviewRequest;
import com.saurav.ArtCorner.response.ApiResponse;
import com.saurav.ArtCorner.service.ProductService;
import com.saurav.ArtCorner.service.ReviewService;
import com.saurav.ArtCorner.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

    private final ReviewService reviewService;
    private final UserService userService;
    private final ProductService productService;

    @GetMapping("/product/{productId}/review")
    public ResponseEntity<List<Review>> getReviewByProductId(@PathVariable Long productId)
    {
        List<Review> reviews=reviewService.getReviewByProductId(productId);
        return ResponseEntity.ok(reviews);
    }

    @PostMapping("/product/{productId}/review")
    public ResponseEntity<Review> writeReview(@RequestBody CreateReviewRequest req,
                                              @PathVariable Long productId,
                                              @RequestHeader("Authentication")String jwt) throws Exception {
        User user=userService.findUserByJwtToken(jwt);
        Product product=productService.findProductById(productId);

        Review review=reviewService.createReview(req,user,product);
        return ResponseEntity.ok(review);
    }

    @PatchMapping("/review/{reviewId}")
    public ResponseEntity<Review> updateReview(
            @RequestBody CreateReviewRequest req,
            @PathVariable Long reviewId,
            @RequestHeader("Authentication")String jwt) throws Exception
    {
        User user=userService.findUserByJwtToken(jwt);
        Review review=reviewService.updateReview(reviewId,req.getReviewText(), (int) req.getReviewRating(),user.getId());
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/review/{reviewId}")
    public ResponseEntity<ApiResponse> deleteReview(
            @PathVariable Long reviewId,
            @RequestHeader ("Authentication")String jwt) throws Exception {
        User user=userService.findUserByJwtToken(jwt);
        reviewService.deleteReview(reviewId, user.getId());
        ApiResponse res=new ApiResponse();
        res.setMessage("Review deleted successfully");
        return ResponseEntity.ok(res);
    }
}
