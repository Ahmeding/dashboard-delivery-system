package com.hitecart.deliverysystem.mapper;

import com.hitecart.deliverysystem.dto.claim.ClaimRequest;
import com.hitecart.deliverysystem.dto.claim.ClaimResponse;
import com.hitecart.deliverysystem.model.Claim;
import com.hitecart.deliverysystem.model.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring",uses = {})
public interface ClaimMapper {


    // Map ClaimRequest → Claim
    @Mapping(target = "reportedBy", source = "reportedByUserId")//
    Claim toClaim(ClaimRequest request);

    // Map Claim → ClaimResponse
    @Mapping(target = "reportedByUserId", source = "reportedBy") //
    ClaimResponse toClaimResponse(Claim claim);

    default User mapReportedBy(Integer reportedByUserId){
        if(reportedByUserId ==null) return null;
        return User.builder().id(reportedByUserId).build();
    }
    default Integer mapReportedByUserId(User reportedBy){
        if(reportedBy == null) return null;
        return reportedBy.getId();
    }
    //  method for updating entity from request
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateClaimFromRequest(ClaimRequest request, @MappingTarget Claim entity);
}

