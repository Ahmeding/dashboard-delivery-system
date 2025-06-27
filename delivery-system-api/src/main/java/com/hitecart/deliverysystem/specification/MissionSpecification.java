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
                    root.join("missionVehicles");
            Join<Object,Object> vehicleChauffeursJoin= missionVehiclesJoin.join("chauffeurVehicleList");
            return criteriaBuilder.equal(vehicleChauffeursJoin.get("driver").get("id"), chauffeurId);
        };
    }

}
