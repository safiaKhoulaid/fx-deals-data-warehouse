package com.progresssoft.warehouse.model;


import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "fx_deals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Deal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "deal_unique_id", nullable = false, unique = true)
    private String dealUniqueId;

    @Column(name = "from_currency_iso", nullable = false, length = 3)
    private String fromCurrencyIsoCode;

    @Column(name = "to_currency_iso", nullable = false, length = 3)
    private String toCurrencyIsoCode;

    @Column(name = "deal_timestamp", nullable = false)
    private Instant timestamp;

    @Column(name = "deal_amount", nullable = false, precision = 19, scale = 4)
    private BigDecimal amount;


}