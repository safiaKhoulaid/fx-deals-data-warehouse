package com.progresssoft.warehouse.repository;

import com.progresssoft.warehouse.model.Deal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface DealRepository extends JpaRepository<Deal, Long> {

    boolean existsByDealUniqueId(String dealUniqueId);

    @Query("SELECT d.dealUniqueId FROM Deal d WHERE d.dealUniqueId IN :ids")
    Set<String> findExistingIds(@Param("ids") List<String> ids);


}
