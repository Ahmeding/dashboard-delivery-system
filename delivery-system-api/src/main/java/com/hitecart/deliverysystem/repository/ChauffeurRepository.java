package com.hitecart.deliverysystem.repository;

import com.hitecart.deliverysystem.dto.ChauffeurDto;
import com.hitecart.deliverysystem.model.Chauffeur;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChauffeurRepository extends JpaRepository<Chauffeur,Integer> {

   Optional<Chauffeur> findChauffeurByEmail(String email);
   List<Chauffeur> findAllByEnabled(Boolean isActive);
   @Query("""
            SELECT chauffeur
            FROM Chauffeur chauffeur
          """)
    Page<Chauffeur> findAllChauffeurs(Pageable pageable);

    Chauffeur save(Chauffeur requestChauffeur);

    @Modifying
    @Query("UPDATE Chauffeur c SET c.isAssigned =:isAssigned WHERE c.id IN :ids")
    void bulkUpdateAssignmentStatus(
            @Param("ids") List<Integer> ids,
            @Param("isAssigned") boolean isAssigned);


}
