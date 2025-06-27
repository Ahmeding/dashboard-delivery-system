package com.hitecart.deliverysystem.dao;

import com.hitecart.deliverysystem.model.DeliveryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryServiceRepository extends JpaRepository<DeliveryService, Integer> {
    @Query("""
                SELECT sd
                FROM DeliveryService sd
            """)
    Page<DeliveryService> findAllServicesDelivery(Pageable pageable);

    List<DeliveryService> findAllByEnabled(boolean enabled);

    @Query("""
                SELECT ds
                FROM DeliveryService ds
                WHERE ds.destinationCity = :destinationCity
                AND ds.destinationCountry = :destinationCountry
                AND ds.id <> :excludeId
            """)
    Optional<DeliveryService> findDeliveryServiceByDestinationCityAndAndDestinationCountry(
            @Param("destinationCity") String destinationCity,
            @Param("destinationCountry") String destinationCountry,
            @Param("excludeId") Integer excludeId);
}
