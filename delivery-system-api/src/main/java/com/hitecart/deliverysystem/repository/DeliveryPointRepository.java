package com.hitecart.deliverysystem.repository;


import com.hitecart.deliverysystem.model.DeliveryPoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeliveryPointRepository extends JpaRepository<DeliveryPoint, Integer> {

    Optional<DeliveryPoint> findDeliveryPointByCity(String city);

    @Query("""
            SELECT deliveryPoint
            FROM DeliveryPoint deliveryPoint
        """)
    Page<DeliveryPoint> findAllDeliveryPoints(Pageable pageable);
}
