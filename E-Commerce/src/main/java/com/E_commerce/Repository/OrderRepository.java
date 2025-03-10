package com.E_commerce.Repository;

import com.E_commerce.Entity.Order;
import com.E_commerce.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

//used in order service
List<Order> findByUser(User user);
    @Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.status = 'DELIVERED'")
    Long getTotalRevenue();
}




