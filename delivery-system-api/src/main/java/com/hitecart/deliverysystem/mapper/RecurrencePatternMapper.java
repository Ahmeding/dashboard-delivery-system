package com.hitecart.deliverysystem.mapper;

import com.hitecart.deliverysystem.dto.recurrentpattern.RecurrencePatternRequest;
import com.hitecart.deliverysystem.dto.recurrentpattern.RecurrencePatternResponse;
import com.hitecart.deliverysystem.model.RecurrencePattern;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.WARN)
public interface RecurrencePatternMapper {

    RecurrencePattern toEntity(RecurrencePatternRequest request);
    RecurrencePatternResponse toResponse(RecurrencePattern entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRecurrencePatternFromRequest(RecurrencePatternRequest request, @MappingTarget RecurrencePattern recurrencePattern);
}
