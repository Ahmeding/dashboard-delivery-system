package com.hitecart.deliverysystem.repository;


import com.hitecart.deliverysystem.model.DeliveryPoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryPointRepository extends JpaRepository<DeliveryPoint, Integer> {

    List<DeliveryPoint> findAllByAddress_Ville(String city);

    @Query("""
            SELECT deliveryPoint
            FROM DeliveryPoint deliveryPoint
        """)
    Page<DeliveryPoint> findAllDeliveryPoints(Pageable pageable);

 @Query("""
            SELECT deliveryPoint
            FROM DeliveryPoint deliveryPoint
            WHERE  deliveryPoint.deliveryDestination.serviceDelivery.id = :serviceDeliveryId
        """)
    List<DeliveryPoint> findByClientAndServiceDelivery(Integer serviceDeliveryId);
}
/*

*/
