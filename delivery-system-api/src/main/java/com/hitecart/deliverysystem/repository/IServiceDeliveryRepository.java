package com.hitecart.deliverysystem.repository;

import com.hitecart.deliverysystem.model.ServiceDelivery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface IServiceDeliveryRepository extends JpaRepository<ServiceDelivery,Integer> {
    @Query("""
            SELECT sd
            FROM ServiceDelivery sd
        """)
    Page<ServiceDelivery> findAllServicesDelivery(Pageable pageable);

    List<ServiceDelivery> findAllByEnabled(boolean enabled);
}
