package com.hitecart.deliverysystem.dao;

import com.hitecart.deliverysystem.model.DeliveryPointCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeliveryPointCategoryRepository extends JpaRepository<DeliveryPointCategory, Integer> {

    DeliveryPointCategory findByName(String name);

    @Query("""
            SELECT dpC
            FROM DeliveryPointCategory dpC
           """)
    Page<DeliveryPointCategory> findAllDeliveryPointCategories(Pageable pageable);

    @Query("""
            SELECT dpC
            FROM DeliveryPointCategory dpC
            WHERE dpC.name = :name AND dpC.additionalFees = :additionalFees
           """)
    Optional<DeliveryPointCategory> findByNameAndAdditionalFees(
            @Param("name") String name,
            @Param("additionalFees") Double additionalFees);
}
