package com.hitecart.deliverysystem.dao;

import com.hitecart.deliverysystem.model.Quote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface QuoteRepository extends JpaRepository<Quote, Integer> {

    List<Quote> findAllByClientId(Integer id);

    Optional<Quote> findQuoteByCode(String code);

    @Query("""
            SELECT quote
            FROM Quote quote
            """)
    Page<Quote> findAllQuotes(Pageable pageable);
}
