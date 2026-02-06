package com.progresssoft.warehouse.service;

import com.progresssoft.warehouse.dto.DealRequestDTO;

import java.util.List;

/**
 * Service interface for handling FX deals business logic.
 * Follows SRP by focusing only on data validation and persistence.
 */

public interface DealService {

    /**
     * Processes a list of deals, ensuring idempotency and resilience (No Rollback).
     * * @param deals List of deal DTOs to be imported.
     */

  void importDeals(List<DealRequestDTO> deals) ;



}
