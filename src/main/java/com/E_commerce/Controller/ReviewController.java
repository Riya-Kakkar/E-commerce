package com.E_commerce.Controller;

import com.E_commerce.Entity.Review;
import com.E_commerce.Entity.User;
import com.E_commerce.Model.ReviewAddDTO;
import com.E_commerce.Repository.UserRepository;
import com.E_commerce.Service.ProductService;
import com.E_commerce.Service.ReviewService;
import com.E_commerce.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//localhost:9090/e-commerce/reviews

@RestController
@RequestMapping("/e-commerce/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserRepository userRepository;


    @PostMapping("/add")
    public ResponseEntity<Review> addReview(@Valid @RequestBody ReviewAddDTO reviewAddDTO) {
            Review review = reviewService.addReview(reviewAddDTO);
            return  ResponseEntity.ok(review) ;
    }


    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getProductReviews(@PathVariable int productId) {
        return ResponseEntity.ok(reviewService.getProductReviews(productId));
    }


    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable int reviewId , @RequestParam int adminId) {

        reviewService.deleteReview(reviewId, adminId);

        return ResponseEntity.ok("Review deleted successfully.");

    }

    @GetMapping("/average/{productId}")
    public ResponseEntity<Double> getAverageRating(@PathVariable int productId) {
        double averageRating = reviewService.calculateAverageRating(productId);
        return ResponseEntity.ok(averageRating);
    }


    @PutMapping("/markInappropriate/{reviewId}")
    public ResponseEntity<Review> markReviewAsInappropriate(@PathVariable int reviewId) {

        Review review = reviewService.markReviewAsInappropriate(reviewId);
        return ResponseEntity.ok(review);
    }

}
