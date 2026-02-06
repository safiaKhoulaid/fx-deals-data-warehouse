package com.progresssoft.warehouse.dto;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public record ImportReportDTO(
        AtomicInteger totalItems,
        AtomicInteger successCount,
        AtomicInteger duplicateCount,
        List<DealRequestDTO> failedDails
) {
}
