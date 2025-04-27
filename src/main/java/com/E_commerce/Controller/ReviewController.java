package com.E_commerce.Controller;

import com.E_commerce.Entity.Review;
import com.E_commerce.Model.ReviewAddDTO;
import com.E_commerce.Service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//localhost:9090/e-commerce/reviews

@RestController
@RequestMapping("/e-commerce/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;


    @PostMapping("/add")
    public ResponseEntity<Review> addReview(@Valid @RequestBody ReviewAddDTO reviewAddDTO , Authentication authentication) {
            Review review = reviewService.addReview(reviewAddDTO ,authentication);
            return  ResponseEntity.ok(review) ;
    }


    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getProductReviews(@PathVariable int productId ) {
        return ResponseEntity.ok(reviewService.getProductReviews(productId));
    }

    @GetMapping("/average/{productId}")
    public ResponseEntity<Double> getAverageRating(@PathVariable int productId ) {
        double averageRating = reviewService.getAverageRating(productId);
        return ResponseEntity.ok(averageRating);
    }

    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable int reviewId ) {

        reviewService.deleteReview(reviewId);

        return ResponseEntity.ok("Review deleted successfully.");

    }

    @PostMapping("/markInappropriate/{reviewId}")
    public ResponseEntity<Review> markReviewAsInappropriate(@PathVariable int reviewId ) {

        Review review = reviewService.markReviewAsInappropriate(reviewId);
        return ResponseEntity.ok(review);
    }

}
