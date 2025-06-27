package com.hitecart.deliverysystem.mapper;


import com.hitecart.deliverysystem.dto.client.ClientRequest;
import com.hitecart.deliverysystem.dto.client.ClientResponse;
import com.hitecart.deliverysystem.dto.client.ClientUpdateRequest;
import com.hitecart.deliverysystem.dto.driver.DriverRequest;
import com.hitecart.deliverysystem.model.*;
import org.mapstruct.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        uses = {AddressMapper.class,
                RoleMapper.class
})
public interface ClientMapper {

    @Mapping(target = "roles", source = "roles")
    @Mapping(target = "notifications", ignore = true)
    Client toClient(ClientRequest request);

    @Mapping(target = "address", source = "address")
    @Mapping(target = "roles", source = "roles")
    ClientResponse toClientResponse(Client client);

    // ID lists → Entity lists
    // Explicitly define generic types in custom methods




    //  method for updating entity from request
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateClientFromRequest(ClientUpdateRequest request, @MappingTarget Client entity);



}
