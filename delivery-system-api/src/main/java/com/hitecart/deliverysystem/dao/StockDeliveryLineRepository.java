package com.hitecart.deliverysystem.dao;

import com.hitecart.deliverysystem.model.StockDeliveryLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockDeliveryLineRepository extends JpaRepository<StockDeliveryLine, Integer> {
    boolean existsByStoredGoodsId(Integer storedGoodsId);
}
