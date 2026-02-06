package com.progresssoft.warehouse.controllers;

import com.progresssoft.warehouse.dto.DealRequestDTO;
import com.progresssoft.warehouse.dto.ImportReportDTO;
import com.progresssoft.warehouse.service.IDealService;
import com.progresssoft.warehouse.utils.CsvParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DealController.class) // 1. كنحملو غير الـ Controller بوحدو
class DealControllerTest {

    @Autowired
    private MockMvc mockMvc; // الأداة باش نصيفطو Request

    @MockBean
    private IDealService dealService; // كنكذبوا على السيرفيس

    @MockBean
    private CsvParser csvParser; // كنكذبوا على الـ Parser

    // --- Test 1: JSON Endpoint ---
    @Test
    @DisplayName("POST /api/deals - Should import JSON list successfully")
    void shouldImportJsonDeals() throws Exception {
        // Arrange
        ImportReportDTO mockReport = new ImportReportDTO(1, 1, 0, Collections.emptyList());
        when(dealService.importDeals(anyList())).thenReturn(mockReport);

        String jsonContent = """
                [
                    {
                        "dealUniqueId": "DEAL-001",
                        "fromCurrencyIsoCode": "USD",
                        "toCurrencyIsoCode": "JOD",
                        "timestamp": "2023-10-01T10:00:00Z",
                        "amount": 1000
                    }
                ]
                """;

        // Act & Assert
        mockMvc.perform(post("/api/deals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isOk()) // كنتسناو 200 OK
                .andExpect(jsonPath("$.successCount").value(1)); // كنتسناو التقرير يرجع 1
    }

    // --- Test 2: CSV Endpoint ---
    @Test
    @DisplayName("POST /api/deals/upload - Should upload CSV file successfully")
    void shouldUploadCsvFile() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
                "file",           // سمية الـ Parameter
                "deals.csv",      // سمية الملف
                "text/csv",       // النوع
                "header...".getBytes() // المحتوى (ماكيهمش حيت غانموكيوه)
        );

        // كنقولو للـ Parser: "إلا جاك شي ملف، غير رد ليا ليستة فيها Deal واحد"
        List<DealRequestDTO> mockDeals = List.of(
                new DealRequestDTO("D1", "USD", "EUR", Instant.now(), BigDecimal.TEN)
        );
        when(csvParser.parseDeals(any())).thenReturn(mockDeals);

        // كنقولو للـ Service: "إلا جاتك ليستة، رد ليا تقرير ناجح"
        when(dealService.importDeals(any())).thenReturn(new ImportReportDTO(1, 1, 0, Collections.emptyList()));

        // Act & Assert
        mockMvc.perform(multipart("/api/deals/upload").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalItems").value(1));
    }
}