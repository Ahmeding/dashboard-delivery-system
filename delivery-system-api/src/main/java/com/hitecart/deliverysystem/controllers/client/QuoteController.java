package com.hitecart.deliverysystem.controllers.client;

import com.hitecart.deliverysystem.common.PageResponse;
import com.hitecart.deliverysystem.controllers.api.QuoteApi;
import com.hitecart.deliverysystem.dto.quote.QuoteRequest;
import com.hitecart.deliverysystem.dto.quote.QuoteResponse;
import com.hitecart.deliverysystem.services.QuoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class QuoteController implements QuoteApi {

    private final QuoteService quoteService;

    @Override
    public ResponseEntity<QuoteResponse> saveQuote(QuoteRequest quoteRequest) {
        return ResponseEntity.ok(quoteService.save(quoteRequest));
    }

    @Override
    public ResponseEntity<QuoteResponse> updateQuoteInfo(Integer quoteId, QuoteRequest quoteRequest) {
        return ResponseEntity.ok(quoteService.updateQuteInfo(quoteId, quoteRequest));
    }

    @Override
    public ResponseEntity<QuoteResponse> findQuoteById(Integer id) {
        return ResponseEntity.ok(quoteService.findById(id));
    }

    @Override
    public ResponseEntity<QuoteResponse> findQuoteByCode(String code) {
        return ResponseEntity.ok(quoteService.findByCode(code));
    }

    @Override
    public ResponseEntity<List<QuoteResponse>> findAllQuote() {
        return ResponseEntity.ok(quoteService.findAll());
    }

    @Override
    public  ResponseEntity<?> deleteQuote(Integer id) {
        try {
            quoteService.delete(id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "status", "success",
                            "message", "Quote deleted successfully"
                    ));
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "status", "error",
                            "message", "Quote not found with ID: " + id
                    ));
        }
    }

    @Override
    public ResponseEntity<PageResponse<QuoteResponse>> findAllQuotesPageable(int page, int size) {
        return ResponseEntity.ok(quoteService.findAllQuotes(page,size));
    }
}
