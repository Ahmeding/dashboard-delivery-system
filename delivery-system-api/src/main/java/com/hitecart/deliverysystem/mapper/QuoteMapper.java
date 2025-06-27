package com.hitecart.deliverysystem.mapper;

import com.hitecart.deliverysystem.dto.quote.QuoteRequest;
import com.hitecart.deliverysystem.dto.quote.QuoteResponse;
import com.hitecart.deliverysystem.dto.recurrentpattern.RecurrencePatternRequest;
import com.hitecart.deliverysystem.model.Client;
import com.hitecart.deliverysystem.model.Order;
import com.hitecart.deliverysystem.model.Quote;
import com.hitecart.deliverysystem.model.RecurrencePattern;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {
        ClientMapper.class,
        //QuoteLineMapper.class
}, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface QuoteMapper {

    @Mapping(target = "quoteLines", source = "quoteLines")
    @Mapping(target = "client", source = "clientId")
    Quote toEntity(QuoteRequest request);

    @Mapping(target = "quoteLines", source = "quoteLines")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "lastModifiedBy", source = "lastModifiedBy")
    QuoteResponse toResponse(Quote entity);

/*    default Quote mapQuote(Integer quoteId) {
        if (quoteId == null) return null;
        return Quote.builder().id(quoteId).build();
    }*/
    // Map clientId → Client entity
    default Client mapClientIdToClient(Integer clientId) {
        if (clientId == null) return null;
        return Client.builder().id(clientId).build();
    }
    @AfterMapping
    default void linkOrderLinesToOrder(Quote quote) {
        if(quote.getQuoteLines() != null){
            quote.getQuoteLines().forEach(quoteLine -> quoteLine.setQuote(quote));
        }
    }


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateQuoteFromRequest(QuoteRequest request, @MappingTarget Quote quote);

}
