package com.E_commerce.Service;

import com.E_commerce.Entity.Product;
import com.E_commerce.Entity.Review;
import com.E_commerce.Entity.User;
import com.E_commerce.Helper.ProductNotFoundException;
import com.E_commerce.Helper.ReviewNotFoundException;
import com.E_commerce.Helper.UnauthorizedAccessException;
import com.E_commerce.Model.ReviewAddDTO;
import com.E_commerce.Repository.ProductRepository;
import com.E_commerce.Repository.ReviewRepository;
import com.E_commerce.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;


    public Review addReview(ReviewAddDTO reviewAddDTO, Authentication authentication ) {

        String username = authentication.getName();

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Product product = productRepository.findById(reviewAddDTO.productId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        boolean hasPurchased = orderService.checkIfUserHasPurchasedProduct(user, product);
        if (!hasPurchased) {
            throw new IllegalStateException("User has not purchased this product.");
        }

        List<Review> existingReviews = reviewRepository.findByUserIdAndProductId(user.getId(), product.getId());
        if (!existingReviews.isEmpty()) {
            throw new IllegalStateException("User has already reviewed this product.");
        }

        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setRating(reviewAddDTO.rating());
        review.setComment(reviewAddDTO.comment());
        review.setCreatedAt(LocalDateTime.now());

        return reviewRepository.save(review);
    }

    public double getAverageRating(int productId) {

        List<Review> reviews = reviewRepository.findByProductId(productId);

        return reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

    }

    // all reviews product
    public List<Review> getProductReviews(int productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return reviewRepository.findByProduct(product);
    }

    //mark Review As Inappropriate
    public Review markReviewAsInappropriate(int reviewId ) {


        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found with ID: " + reviewId));

            review.setInappropriate(true);
            return reviewRepository.save(review);
    }

    //delete by admin
    public void deleteReview(int reviewId ) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found"));

        if (!review.isInappropriate()) {
            throw new IllegalStateException("Review is not inappropriate, cannot delete.");
        }


        reviewRepository.delete(review);
    }
}
