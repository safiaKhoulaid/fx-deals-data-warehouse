package com.progresssoft.warehouse.mapper;

import com.progresssoft.warehouse.dto.DealRequestDTO;
import com.progresssoft.warehouse.model.Deal;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class DealMapperTest {

    
    private final DealMapper mapper = Mappers.getMapper(DealMapper.class);

    @Test
    void shouldMapDtoToEntityCorrectly() {
        
        DealRequestDTO dto = new DealRequestDTO(
                "DEAL-001", "USD", "JOD", Instant.now(), BigDecimal.valueOf(100.50)
        );

        
        Deal entity = mapper.toEntity(dto);

        
        assertNotNull(entity);
        assertEquals(dto.dealUniqueId(), entity.getDealUniqueId());
        assertEquals(dto.fromCurrencyIsoCode(), entity.getFromCurrencyIsoCode());
        assertEquals(dto.toCurrencyIsoCode(), entity.getToCurrencyIsoCode());
        assertEquals(dto.timestamp(), entity.getTimestamp());
        assertEquals(dto.amount(), entity.getAmount());
    }

    @Test
    void shouldReturnNullWhenDtoIsNull() {
        assertNull(mapper.toEntity(null));
    }
}