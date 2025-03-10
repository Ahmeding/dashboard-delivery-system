package com.hitecart.deliverysystem.repository;

import com.hitecart.deliverysystem.model.DeliveryPointCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IDeliveryPointCategoryRepository extends JpaRepository<DeliveryPointCategory, Integer> {

    DeliveryPointCategory findByName(String name);

    @Query("""
            SELECT dpC
            FROM DeliveryPointCategory dpC
           """)
    Page<DeliveryPointCategory> findAllDeliveryPointCategories(Pageable pageable);
}
