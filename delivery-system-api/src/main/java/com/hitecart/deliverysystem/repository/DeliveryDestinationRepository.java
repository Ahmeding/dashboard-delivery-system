package com.hitecart.deliverysystem.repository;


import com.hitecart.deliverysystem.dto.DeliveryDestinationDto;
import com.hitecart.deliverysystem.model.DeliveryDestination;
import com.hitecart.deliverysystem.model.DeliveryPoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryDestinationRepository extends JpaRepository<DeliveryDestination, Integer> {


    @Query("""
            SELECT ds
            FROM DeliveryDestination ds
        """)
    Page<DeliveryPoint> findAllDeliveryDestinations(Pageable pageable);


/*    @Query("""
            SELECT ds
            FROM DeliveryDestination ds
            WHERE ds.client.id = :clientId
        """)
    List<DeliveryDestination> findAllByClientId(Integer clientId);*/




    @Query("""
            SELECT dd
            FROM DeliveryDestination dd
            WHERE dd.serviceDelivery.id = :serviceDeliveryId
        """)
    DeliveryDestination findDeliveryDestinationsByClientIdAndServiceDeliveryId(Integer serviceDeliveryId);

  @Query("""
            SELECT dd
            FROM DeliveryDestination dd
            WHERE dd.serviceDelivery.id = :serviceDeliveryId
        """)
    DeliveryDestination findDeliveryDestinationsByServiceDeliveryId(Integer serviceDeliveryId);

}
