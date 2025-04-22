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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/*ReviewService (Handles product reviews and ratings)*/

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


    public Review addReview(ReviewAddDTO reviewAddDTO) {

        User user = userRepository.findById(reviewAddDTO.userId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Product product = productRepository.findById(reviewAddDTO.productId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        boolean hasPurchased = orderService.checkIfUserHasPurchasedProduct(user, product);
        if (!hasPurchased) {
            throw new IllegalStateException("User has not purchased this product.");
        }

        List<Review> existingReviews = reviewRepository.findByUserIdAndProductId(user.getId(), product.getId());
        if (existingReviews.isEmpty()) {
            throw new IllegalStateException("User has already reviewed this product.");
        }

        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setRating(reviewAddDTO.rating());
        review.setComment(reviewAddDTO.comment());

        return reviewRepository.save(review);
    }

    // all reviews product
    public List<Review> getProductReviews(int productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return reviewRepository.findByProduct(product);
    }

    //delete by admin
    public void deleteReview(int reviewId, int adminId) {

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!"ADMIN".equals(admin.getRole())) {
            throw new UnauthorizedAccessException("Only admins can delete reviews.");
        }

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found"));

        reviewRepository.delete(review);
    }

    public double calculateAverageRating(int productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        List<Review> reviews = reviewRepository.findByProductId(product.getId());

        if (reviews.isEmpty()) {
            return 0.0;
        }

        int totalRating = 0;
        for (Review review : reviews) {
            totalRating += review.getRating();
        }
        return (double)  totalRating / reviews.size();
    }

    //optional
    public Review markReviewAsInappropriate(int reviewId) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewNotFoundException("Review not found with ID: " + reviewId));

            review.setInappropriate(true);
            return reviewRepository.save(review);
    }

}
