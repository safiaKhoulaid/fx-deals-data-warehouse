package com.progresssoft.warehouse.dto;


public record FailedDealDTO(
        DealRequestDTO deal,
        String failureReason
) {}
