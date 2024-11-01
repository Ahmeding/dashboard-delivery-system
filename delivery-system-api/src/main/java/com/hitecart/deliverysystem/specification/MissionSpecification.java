package com.hitecart.deliverysystem.specification;

import com.hitecart.deliverysystem.model.Mission;
import com.hitecart.deliverysystem.model.MissionVehicle;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class MissionSpecification {
    public static Specification<Mission> withChauffeurId(Integer chauffeurId) {
        return (root,query, criteriaBuilder)->{
            Join<Object,Object> missionVehiclesJoin=
                    root.join("missionVehicules");
            Join<Object,Object> vehicleChauffeursJoin= missionVehiclesJoin.join("chauffeurVehiculeList");
            return criteriaBuilder.equal(vehicleChauffeursJoin.get("chauffeur").get("id"), chauffeurId);
        };
    }

}
