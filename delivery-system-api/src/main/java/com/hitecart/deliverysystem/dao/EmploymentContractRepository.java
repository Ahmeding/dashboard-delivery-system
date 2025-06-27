package com.hitecart.deliverysystem.dao;

import com.hitecart.deliverysystem.model.EmploymentContract;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmploymentContractRepository extends JpaRepository<EmploymentContract, Integer> {

        List<EmploymentContract> findAllByDriverId(Integer idChauffeur);

        @Query("""
                SELECT employmentContract
                FROM EmploymentContract employmentContract
            """)
    Page<EmploymentContract> findAllContracts(Pageable pageable);
}
