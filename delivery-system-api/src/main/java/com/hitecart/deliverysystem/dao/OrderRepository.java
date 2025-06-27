package com.hitecart.deliverysystem.dao;

import com.hitecart.deliverysystem.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer> {

    List<Order> findAllByClientId(Integer id);

    Optional<Order> findOrderByCode(String code);

    @Query("""
        SELECT order
        FROM Order order
        """)
    Page<Order> findAllOrders(Pageable pageable);
}
