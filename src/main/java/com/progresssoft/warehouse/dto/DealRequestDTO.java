package com.progresssoft.warehouse.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.Instant;

public record DealRequestDTO(

        @NotBlank(message = "Deal Unique ID is mandatory")
        String dealUniqueId,

        @NotBlank(message = "From currency ISO code is mandatory")
        @Size(min = 3, max = 3, message = "From currency ISO code must be exctly 3 characters")
        String fromCurrencyIsoCode,

        @NotBlank(message = "To currency ISO code is mandatory")
        @Pattern(regexp = "^[A-Z]{3}$", message = "To currency ISO code must be exactly 3 uppercase letters")
        String toCurrencyIsoCode,

        @NotNull(message = "Deal timestamp is mandatory")
        @PastOrPresent(message = "Deal timestamp cannot be in the future")
        Instant timestamp,

        @NotNull(message = "Amount is mandatory")
        @Positive(message = "Amount must be a positive value")
        BigDecimal amount


) {
}
