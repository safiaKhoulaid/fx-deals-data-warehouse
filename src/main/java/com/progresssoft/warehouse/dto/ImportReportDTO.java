package com.progresssoft.warehouse.dto;

import java.util.List;

public record ImportReportDTO(
        int totalItems,
        int successCount,
        int duplicateCount,
        List<DealRequestDTO> failedDails
) {
}
