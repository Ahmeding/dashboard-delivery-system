package com.hitecart.deliverysystem.mapper;

import com.hitecart.deliverysystem.dto.company.CompanyRequest;
import com.hitecart.deliverysystem.dto.company.CompanyResponse;
import com.hitecart.deliverysystem.dto.driver.DriverRequest;
import com.hitecart.deliverysystem.model.Company;
import com.hitecart.deliverysystem.model.Driver;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {
        AddressMapper.class,

},unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CompanyMapper {

    // Map CompanyRequest → Company
    @Mapping(target = "address", source = "address") // Uses AddressMapper
    Company toCompany(CompanyRequest request);

    // Map Company → CompanyResponse
    @Mapping(target = "address", source = "address") // Uses AddressMapper
    CompanyResponse toCompanyResponse(Company company);

    //  method for updating entity from request
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCompanyFromRequest(CompanyRequest request, @MappingTarget Company entity);
}
