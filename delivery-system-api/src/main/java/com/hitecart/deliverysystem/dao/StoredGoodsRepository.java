package com.hitecart.deliverysystem.dao;

import com.hitecart.deliverysystem.model.StoredGoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoredGoodsRepository extends JpaRepository<StoredGoods, Integer> {

    List<StoredGoods> findAllByClientId(Integer clientId);

    Optional<StoredGoods> findByReference(String reference);
}
