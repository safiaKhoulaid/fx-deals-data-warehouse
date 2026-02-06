package com.progresssoft.warehouse.service;

import com.progresssoft.warehouse.dto.DealRequestDTO;
import com.progresssoft.warehouse.dto.ImportReportDTO;

import java.util.List;

/**
 * Service interface for handling FX deals business logic.
 * Follows SRP by focusing only on data validation and persistence.
 */

public interface IDealService {

    /**
     * Processes a list of deals, ensuring idempotency and resilience (No Rollback).
     * * @param deals List of deal DTOs to be imported.
     */

    ImportReportDTO importDeals(List<DealRequestDTO> deals);


}
