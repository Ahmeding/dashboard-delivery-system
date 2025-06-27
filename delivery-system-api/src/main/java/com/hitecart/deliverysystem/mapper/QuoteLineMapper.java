package com.hitecart.deliverysystem.mapper;

import com.hitecart.deliverysystem.dto.quoteline.QuoteLineRequest;
import com.hitecart.deliverysystem.dto.quoteline.QuoteLineResponse;
import com.hitecart.deliverysystem.dto.recurrentpattern.RecurrencePatternRequest;
import com.hitecart.deliverysystem.model.DeliveryService;
import com.hitecart.deliverysystem.model.Quote;
import com.hitecart.deliverysystem.model.QuoteLine;
import com.hitecart.deliverysystem.model.RecurrencePattern;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {
        DeliveryServiceMapper.class,
        QuoteMapper.class
}, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface QuoteLineMapper {

    @Mapping(target = "deliveryService", source = "deliveryServiceId")
    @Mapping(target = "quote", source = "quoteId",qualifiedByName = "mapQuote")
    QuoteLine toEntity(QuoteLineRequest request);


    @Mapping(target = "deliveryService", source = "deliveryService")
    @Mapping(target = "quote", source = "quote")
    QuoteLineResponse toResponse(QuoteLine entity);

    // Convert IDs to entities
    @Named("mapQuote")
    default Quote mapQuote(Integer quoteId){
        if(quoteId == null) return null;
        return Quote.builder().id(quoteId).build();
    }

    default DeliveryService mapDeliveryService(Integer dsId){
        if(dsId == null) return null;
        return DeliveryService.builder().id(dsId).build();
    }


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateQuoteLineFromRequest(QuoteLineRequest request, @MappingTarget QuoteLine quoteLine);

}
