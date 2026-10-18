package com.hitecart.deliverysystem.dao;

import com.hitecart.deliverysystem.model.StockDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockDeliveryRepository extends JpaRepository<StockDelivery,Integer> {

    List<StockDelivery> findAllByClientIdOrderByPlannedDateDesc(Integer clientId);

    Optional<StockDelivery> findByReference(String reference);

}
