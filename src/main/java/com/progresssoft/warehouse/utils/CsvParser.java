package com.progresssoft.warehouse.utils;

import com.progresssoft.warehouse.dto.DealRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class CsvParser {

    public List<DealRequestDTO> parseDeals(MultipartFile file) {

        try (var reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

            return reader
                    .lines()
                    .skip(1)
                    .filter(line -> line != null && !line.isBlank())
                    .map(this::mapToDto)
                    .filter(Objects::nonNull)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Critical error reading CSV file : {}" + e.getMessage());
        }
    }

    private DealRequestDTO mapToDto(String line) {
        try {

            String[] data = line.split(",");

            if (data.length < 5) {
                log.warn("Skipping malformed line (insufficient columns ) : {}", line);
                return null;

            }
            return new DealRequestDTO(
                    data[0].trim(),
                    data[1].trim(),
                    data[2].trim(),
                    Instant.parse(data[3].trim()),
                    new BigDecimal(data[4].trim())
            );

        } catch (Exception e) {
            log.error("Skipping row due to parsing error: [{}] - Reason: {}", line, e.getMessage());
            return null;
        }
    }


}