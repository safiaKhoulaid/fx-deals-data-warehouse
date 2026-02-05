package com.progresssoft.warehouse.repository;

import com.progresssoft.warehouse.model.Deal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DealRepository extends JpaRepository<Deal , Long> {


}
