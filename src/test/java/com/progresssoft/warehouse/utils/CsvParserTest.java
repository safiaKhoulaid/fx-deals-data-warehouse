package com.progresssoft.warehouse.utils;

import com.progresssoft.warehouse.dto.DealRequestDTO;
import com.progresssoft.warehouse.exception.customException.CsvImportException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CsvParserTest {

    @InjectMocks
    private CsvParser csvParser;

    
    @Test
    @DisplayName("Should parse valid CSV file successfully")
    void parseDeals_Success() {
        String csvContent = """
                DEAL_ID,FROM_CURRENCY,TO_CURRENCY,TIMESTAMP,AMOUNT
                DEAL-001,USD,JOD,2023-10-01T10:00:00Z,100.50
                """;

        MockMultipartFile file = new MockMultipartFile(
                "file", "deals.csv", "text/csv", csvContent.getBytes()
        );

        List<DealRequestDTO> result = csvParser.parseDeals(file);

        assertEquals(1, result.size());
        assertEquals("DEAL-001", result.get(0).dealUniqueId());
    }

    
    @Test
    @DisplayName("Should throw exception if file is empty")
    void parseDeals_EmptyFile() {
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file", "empty.csv", "text/csv", new byte[0]
        );

        CsvImportException exception = assertThrows(CsvImportException.class, () -> {
            csvParser.parseDeals(emptyFile);
        });

        
        assertEquals("Cannot process an empty CSV file.", exception.getMessage());
    }

    
    @Test
    @DisplayName("Should skip invalid rows instead of throwing exception")
    void parseDeals_InvalidFormat() {
        
        String invalidContent = """
                DEAL_ID,FROM_CURRENCY,TO_CURRENCY,TIMESTAMP,AMOUNT
                DEAL-001,USD,JOD,INVALID-DATE,100
                """;

        MockMultipartFile file = new MockMultipartFile(
                "file", "invalid.csv", "text/csv", invalidContent.getBytes()
        );

        
        List<DealRequestDTO> result = csvParser.parseDeals(file);

        assertTrue(result.isEmpty(), "Should skip the invalid row and return empty list");
    }

    
    @Test
    @DisplayName("Should handle IOException during file reading")
    void parseDeals_IOException() throws IOException {
        MultipartFile badFile = mock(MultipartFile.class);
        when(badFile.isEmpty()).thenReturn(false);
        when(badFile.getInputStream()).thenThrow(new IOException("Disk error"));

        CsvImportException exception = assertThrows(CsvImportException.class, () -> {
            csvParser.parseDeals(badFile);
        });

        
        
        assertTrue(exception.getMessage().contains("Critical error reading CSV file"),
                "Actual message was: " + exception.getMessage());
    }
}