package com.hitecart.deliverysystem.dao;

import com.hitecart.deliverysystem.model.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {

    Optional<Company> findCompaniesByName(String raisonSocial);

    @Query("""
            SELECT company
            FROM Company company
            """)
    Page<Company> findAllCompanies(Pageable pageable);
    @Query("""
            SELECT c
            FROM Company c
            WHERE c.name = :name
            AND c.id <> :excludeId
            """)
    Optional<Company> findCompanyByName(
            @Param("name") String name,
            @Param("excludeId") Integer excludeId);
}
