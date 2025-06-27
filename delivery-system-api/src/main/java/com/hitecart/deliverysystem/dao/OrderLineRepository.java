package com.hitecart.deliverysystem.dao;

import com.hitecart.deliverysystem.model.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.springframework.stereotype.Repository;
@Repository
public interface OrderLineRepository extends JpaRepository<OrderLine, Integer> {

    List<OrderLine> findAllByOrderId(Integer orderId);
    List<OrderLine> findAllByOrderCode(String codeCmdClient);

}
