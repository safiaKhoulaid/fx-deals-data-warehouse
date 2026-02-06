package com.progresssoft.warehouse.service;

import com.progresssoft.warehouse.dto.DealRequestDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DealService {

    //Import deals from a json list

    void importJson(List<DealRequestDTO> deals);

    //Import deals from a csv file

    void importCsv(MultipartFile file);

}
