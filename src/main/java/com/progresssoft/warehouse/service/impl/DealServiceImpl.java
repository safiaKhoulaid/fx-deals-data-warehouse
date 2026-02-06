package com.progresssoft.warehouse.service.impl;


import com.progresssoft.warehouse.dto.DealRequestDTO;
import com.progresssoft.warehouse.dto.FailedDealDTO;
import com.progresssoft.warehouse.dto.ImportReportDTO;
import com.progresssoft.warehouse.mapper.DealMapper;
import com.progresssoft.warehouse.model.Deal;
import com.progresssoft.warehouse.repository.DealRepository;
import com.progresssoft.warehouse.service.IDealService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Slf4j
public class DealServiceImpl implements IDealService {

    private final DealRepository dealRepository;
    private final DealMapper dealMapper;
    private final Validator validator;


    @Override
    public ImportReportDTO importDeals(List<DealRequestDTO> deals) {

        log.info("Starting processing of {} deals", deals.size());

        AtomicInteger total = new AtomicInteger(deals.size());
        AtomicInteger sucess = new AtomicInteger();
        AtomicInteger duplicates = new AtomicInteger();
        List<FailedDealDTO> failedDeals = new ArrayList<>();

        List<String> incomingIds = deals.stream().map(DealRequestDTO::dealUniqueId).toList();
        Set<String> existingIds = dealRepository.findExistingIds(incomingIds);


        deals.forEach(dto -> {

            try {

                if (existingIds.contains(dto.dealUniqueId())) {
                    duplicates.getAndIncrement();
                    failedDeals.add(new FailedDealDTO(dto, "Deal exists") );
                    return;
                }

                Set<ConstraintViolation<DealRequestDTO>> violations = validator.validate(dto);

                if (!violations.isEmpty()) {
                    String errorMsg = violations.iterator().next().getMessage();
                    log.error("Validation failed for deal {}: {}", dto.dealUniqueId(), errorMsg);
                    failedDeals.add(new FailedDealDTO(dto, errorMsg));
                    return;
                }


                Deal deal = dealMapper.toEntity(dto);

                dealRepository.save(deal);

                existingIds.add(dto.dealUniqueId());

                sucess.getAndIncrement();

                log.info("Successfully saved deal: {}", dto.dealUniqueId());

            } catch (Exception e) {
                log.error("Failed to save deal {}: {}", dto.dealUniqueId(), e.getMessage());
                failedDeals.add(new FailedDealDTO(dto, e.getMessage()));
            }
        });
        log.info("Finished processing batch.");

        return new ImportReportDTO(
                total.get(),
                sucess.get(),
                duplicates.get(),
                failedDeals

        );
    }
}
