package com.E_commerce.Service;

import com.E_commerce.Entity.Product;
import com.E_commerce.Entity.Review;
import com.E_commerce.Entity.User;
import com.E_commerce.Repository.ProductRepository;
import com.E_commerce.Repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ProductRepository productRepository;



    public Review addReview(User user, Product product, int rating, String comment) {

        boolean hasPurchased = orderService.checkIfUserHasPurchasedProduct(user, product);
        if (!hasPurchased) {
            throw new IllegalStateException("User must purchase the product before reviewing.");
        }

        List<Review> existingReviews = reviewRepository.findByUserIdAndProductId(user.id(), product.getId());
        if (existingReviews.isEmpty()) {
            throw new IllegalStateException("User has already reviewed this product.");
        }
        Review review = new Review(user, product, rating, comment);
        return reviewRepository.save(review);
    }

    // all reviews product
    public List<Review> getProductReviews(int productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return reviewRepository.findByProduct(product);
    }


    public void deleteReview(int reviewId) {
        reviewRepository.deleteById(reviewId);
    }


    public int calculateAverageRating(Product product) {

        List<Review> reviews = reviewRepository.findByProductId(product.getId());

        if (reviews.isEmpty()) {
            return 0;
        }
        int totalRating = 0;
        for (Review review : reviews) {
            totalRating += review.getRating();
        }
        return  totalRating / reviews.size();
    }

    public Review markReviewAsInappropriate(int reviewId) {
        Review review = reviewRepository.findById(reviewId).orElse(null);
        if (review != null) {
            review.setInappropriate(true);
            return reviewRepository.save(review);
        }
        return null;
    }
}
