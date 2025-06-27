package com.hitecart.deliverysystem.dao;

import com.hitecart.deliverysystem.model.MissionExpense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface MissionExpensesRepository extends JpaRepository<MissionExpense,Integer> {
    Optional<MissionExpense> findByMissionId(Integer idMission);

    @Query("""
            SELECT missionExpense
            FROM MissionExpense missionExpense
            """)
    Page<MissionExpense> findAllMissionExpenses(Pageable pageable);
    @Query("""
            SELECT me
            FROM MissionExpense me
            WHERE me.name = :name
            AND me.expenseDate = :expenseDate
            AND me.quantity = :quantity
            AND me.cost = :cost
            AND me.id <> :excludeId
            """)
    Optional<MissionExpense> findByNameByDateByQuantityByCost(
            @Param("name") String name,
            @Param("expenseDate") LocalDateTime expenseDate,
            @Param("quantity") BigDecimal quantity,
            @Param("cost") BigDecimal cost,
            @Param("excludeId") Integer excludeId);
}
