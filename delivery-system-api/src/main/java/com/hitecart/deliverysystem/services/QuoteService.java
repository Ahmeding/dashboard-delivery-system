package com.hitecart.deliverysystem.services;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.dto.quote.QuoteRequest;
import com.hitecart.deliverysystem.dto.quote.QuoteResponse;

import java.util.List;

public interface QuoteService {
    QuoteResponse save(QuoteRequest request);
    QuoteResponse updateQuteInfo(Integer quoteId, QuoteRequest request);
    QuoteResponse findById(Integer id);
    QuoteResponse findByCode(String code);
    List<QuoteResponse> findAll();
    void delete(Integer id);

    PageResponse<QuoteResponse> findAllQuotes(int page, int size);
}
