package com.E_commerce.Controller;

import com.E_commerce.Entity.Product;
import com.E_commerce.Entity.Review;
import com.E_commerce.Entity.User;
import com.E_commerce.Service.ProductService;
import com.E_commerce.Service.ReviewService;
import com.E_commerce.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/e-commerce/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;


    @PostMapping("/add")
    public ResponseEntity<Review> addReview(@RequestParam int userId, @RequestParam int productId,
                                            @RequestParam int rating, @RequestParam String comment) {
        try {
            User user = userService.getUserById(userId).orElseThrow(() -> new RuntimeException("User not found"));
            Product product = productService.getProductById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

            System.out.println("Adding review for product with ID: " + productId +
                    ", Rating: " + rating + ", Comment: " + comment);

            Review review = reviewService.addReview(user, product, rating, comment);
            return  ResponseEntity.ok(review) ;

        } catch (Exception e) {
            System.err.println("Error adding review: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }


    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> getProductReviews(@PathVariable int productId) {
        System.out.println("Fetching reviews for product with ID: " + productId);
        return ResponseEntity.ok(reviewService.getProductReviews(productId));
    }


    @PutMapping("/markInappropriate/{reviewId}")
    public ResponseEntity<Review> markReviewAsInappropriate(@PathVariable int reviewId) {

        Review review = reviewService.markReviewAsInappropriate(reviewId);
        if (review != null) {
            return ResponseEntity.ok(review);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable int reviewId , Authentication authentication) {
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
            System.out.println("Deleting review with ID: " + reviewId);
            reviewService.deleteReview(reviewId);
            return ResponseEntity.ok("Review deleted successfully.");
        } else {
            return ResponseEntity.status(403).body("You do not have permission to delete reviews.");
        }
    }

    @GetMapping("/average/{productId}")
    public ResponseEntity<Double> getAverageRating(@PathVariable int productId) {
        Product product = productService.getProductById(productId).orElse(null);

        if (product == null) {
            return ResponseEntity.notFound().build(); // Product not found
        }

        double averageRating = reviewService.calculateAverageRating(product);
        return ResponseEntity.ok(averageRating);
    }

}
