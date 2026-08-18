package com.hitecart.deliverysystem.dao;

import com.hitecart.deliverysystem.model.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FeedBackRepository extends JpaRepository<Feedback, Integer> {
    @Query("""
            SELECT feedback
            FROM Feedback  feedback
            WHERE feedback.driver.id = :driver_id
""")
    Page<Feedback> findAllByDriverId(Integer driver_id, Pageable pageable);

    @Query("""
            SELECT f
            FROM Feedback f
            WHERE f.createdBy = :userId
            AND f.driver.id = :driverId
        """)
    Optional<Feedback> findFeedbackByOwnerIdByDriverId(
            @Param("userId") Integer userId,
            @Param("driverId") Integer driverId);
    @Query("""
            SELECT f
            FROM Feedback f
            WHERE f.driver.id = :driverId
        """)
    List<Feedback> findFeedbackByDriverId(
            @Param("driverId") Integer driverId);

}
