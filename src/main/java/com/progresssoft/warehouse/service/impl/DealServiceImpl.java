package com.progresssoft.warehouse.service.impl;


import com.progresssoft.warehouse.dto.DealRequestDTO;
import com.progresssoft.warehouse.dto.ImportReportDTO;
import com.progresssoft.warehouse.mapper.DealMapper;
import com.progresssoft.warehouse.model.Deal;
import com.progresssoft.warehouse.repository.DealRepository;
import com.progresssoft.warehouse.service.IDealService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Slf4j
public class DealServiceImpl implements IDealService {

    private final DealRepository dealRepository;
    private final DealMapper dealMapper;

    @Override
    public ImportReportDTO importDeals(List<DealRequestDTO> deals) {
        log.info("Starting processing of {} deals", deals.size());

        AtomicInteger total = new AtomicInteger(deals.size());
        AtomicInteger sucess = new AtomicInteger();
        AtomicInteger duplicates = new AtomicInteger();
        List<DealRequestDTO> failedDeals = new ArrayList<>();

        deals.forEach(dto -> {

            try {

                if (dealRepository.existsByDealUniqueId(dto.dealUniqueId())) {
                    log.warn("Deal with ID {} already exists. Skipping...", dto.dealUniqueId());
                    duplicates.getAndIncrement();
                    return;
                }

                Deal deal = dealMapper.toEntity(dto);

                dealRepository.save(deal);

                sucess.getAndIncrement();

                log.info("Successfully saved deal: {}", dto.dealUniqueId());


            } catch (Exception e) {
                log.error("Failed to save deal {}: {}", dto.dealUniqueId(), e.getMessage());
                failedDeals.add(dto);
            }
        });
        log.info("Finished processing batch.");
        return new ImportReportDTO(
                total,
                sucess,
                duplicates,
                failedDeals

        );
    }
}
