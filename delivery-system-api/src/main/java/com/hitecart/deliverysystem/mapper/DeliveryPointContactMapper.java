package com.hitecart.deliverysystem.mapper;


import com.hitecart.deliverysystem.dto.deliverypointcontact.DeliveryPointContactRequest;
import com.hitecart.deliverysystem.dto.deliverypointcontact.DeliveryPointContactResponse;
import com.hitecart.deliverysystem.dto.driver.DriverRequest;
import com.hitecart.deliverysystem.model.DeliveryPointContact;
import com.hitecart.deliverysystem.model.Driver;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN)
public interface DeliveryPointContactMapper {

   DeliveryPointContact toEntity(DeliveryPointContactRequest request);

   DeliveryPointContactResponse toResponse(DeliveryPointContact entity);



   //  method for updating entity from request
   @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
   void updateDeliveryPointContactFromRequest(DeliveryPointContactRequest request, @MappingTarget DeliveryPointContact entity);
}
