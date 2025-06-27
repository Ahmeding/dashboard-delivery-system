package com.hitecart.deliverysystem.dao;


import com.hitecart.deliverysystem.model.DeliveryPoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryPointRepository extends JpaRepository<DeliveryPoint, Integer> {

    List<DeliveryPoint> findAllByAddress_City(String city);

    @Query("""
                SELECT deliveryPoint
                FROM DeliveryPoint deliveryPoint
            """)
    Page<DeliveryPoint> findAllDeliveryPoints(Pageable pageable);

    @Query("""
                SELECT deliveryPoint
                FROM DeliveryPoint deliveryPoint
                WHERE  deliveryPoint.deliveryDestination.deliveryService.id = :serviceDeliveryId
            """)
    List<DeliveryPoint> findDeliveryPointByDeliveryServiceId(@Param("serviceDeliveryId") Integer serviceDeliveryId);

    List<DeliveryPoint> findAllByEnabled(boolean enabled);

    @Query("""
            SELECT dp
            FROM DeliveryPoint dp
            WHERE dp.client.id = :clientId
            """)
    List<DeliveryPoint> findAllByClientId(@Param("clientId") Integer clientId);

    @Query("""
            SELECT dp
            FROM DeliveryPoint dp
            WHERE dp.deliveryDestination.id = :delivDestId
            """)
    List<DeliveryPoint> findDeliveryPointByDeliveryDestinationId(@Param("delivDestId") Integer delivDestId);


    List<DeliveryPoint> findDeliveryPointByDeliveryPointCategoryId(Integer deliveryPointCategoryId);

    @Query("SELECT dp FROM DeliveryPoint dp WHERE dp.id = :deliveryPointId")
    DeliveryPoint findDeliveryPointWithRecurrent(@Param("deliveryPointId") Integer deliveryPointId);

    @Query(""" 
            SELECT dp
            FROM DeliveryPoint dp
            WHERE dp.name = :name
            AND dp.deliveryDestination.deliveryService.destinationCity = :destinationCity
            AND dp.id <> :excludeId
            """)
    Optional<DeliveryPoint> findByNameAndCity(String name, String destinationCity, Integer excludeId);
}
/*

 */
