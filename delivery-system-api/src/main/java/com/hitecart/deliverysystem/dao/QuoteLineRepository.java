package com.hitecart.deliverysystem.dao;

import com.hitecart.deliverysystem.model.QuoteLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.springframework.stereotype.Repository;
@Repository
public interface QuoteLineRepository extends JpaRepository<QuoteLine, Integer> {
    List<QuoteLine> findAllByQuoteId(Integer id);
}
