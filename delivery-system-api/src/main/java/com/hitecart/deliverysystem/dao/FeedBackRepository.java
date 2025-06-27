package com.hitecart.deliverysystem.dao;

import com.hitecart.deliverysystem.model.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeedBackRepository extends JpaRepository<Feedback, Integer> {
    @Query("""
            SELECT feedback
            FROM Feedback  feedback
            WHERE feedback.driver.id = :driver_id
""")
    Page<Feedback> findAllByDriverId(Integer driver_id, Pageable pageable);
}
