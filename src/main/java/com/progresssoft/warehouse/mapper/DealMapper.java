package com.progresssoft.warehouse.mapper;

import com.progresssoft.warehouse.dto.DealRequestDTO;
import com.progresssoft.warehouse.model.Deal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DealMapper {

    @Mapping(target = "id", ignore = true)
    Deal toEntity(DealRequestDTO deal);


}
