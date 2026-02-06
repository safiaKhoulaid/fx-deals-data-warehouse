package com.progresssoft.warehouse.controllers;

import com.progresssoft.warehouse.dto.DealRequestDTO;
import com.progresssoft.warehouse.dto.ImportReportDTO;
import com.progresssoft.warehouse.service.IDealService;
import com.progresssoft.warehouse.utils.CsvParser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/deals")
@RequiredArgsConstructor
public class DealController {

    private final IDealService iDealService;
    private final CsvParser csvParser;

    /**
     * Endpoint 1: Import via JSON List
     */

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ImportReportDTO> importDeals(@RequestBody List<DealRequestDTO> deals) {

        ImportReportDTO report = iDealService.importDeals(deals);
        return ResponseEntity.ok(report);
    }


    /**
     * Import via CSV
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImportReportDTO> uploadCsv(@RequestParam("file") MultipartFile file) {
        List<DealRequestDTO> deals = csvParser.parseDeals(file);
        ImportReportDTO report = iDealService.importDeals(deals);
        return ResponseEntity.ok(report);
    }

}
