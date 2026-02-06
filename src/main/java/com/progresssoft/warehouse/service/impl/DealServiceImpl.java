package com.progresssoft.warehouse.service.impl;


import com.progresssoft.warehouse.dto.DealRequestDTO;
import com.progresssoft.warehouse.mapper.DealMapper;
import com.progresssoft.warehouse.model.Deal;
import com.progresssoft.warehouse.repository.DealRepository;
import com.progresssoft.warehouse.service.IDealService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DealServiceImpl implements IDealService {

    private final DealRepository dealRepository;
    private final DealMapper dealMapper;

    @Override
    public void importDeals(List<DealRequestDTO> deals) {
        log.info("Starting processing of {} deals", deals.size());

        deals.forEach(dto -> {

            try {

                if (dealRepository.existsByDealUniqueId(dto.dealUniqueId())) {
                    log.warn("Deal with ID {} already exists. Skipping...", dto.dealUniqueId());
                    return;
                }

                Deal deal = dealMapper.toEntity(dto);

                dealRepository.save(deal);

                log.info("Successfully saved deal: {}", dto.dealUniqueId());


            } catch (Exception e) {
                log.error("Failed to save deal {}: {}", dto.dealUniqueId(), e.getMessage());
            }
        });
        log.info("Finished processing batch.");
    }
}
