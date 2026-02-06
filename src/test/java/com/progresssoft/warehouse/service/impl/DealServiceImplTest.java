package com.progresssoft.warehouse.service.impl;

import com.progresssoft.warehouse.dto.DealRequestDTO;
import com.progresssoft.warehouse.dto.ImportReportDTO;
import com.progresssoft.warehouse.mapper.DealMapper;
import com.progresssoft.warehouse.model.Deal;
import com.progresssoft.warehouse.repository.DealRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DealServiceImplTest {

    @Mock
    private DealRepository dealRepository;

    @Mock
    private DealMapper dealMapper;

    @Mock
    private Validator validator;

    @InjectMocks
    private DealServiceImpl dealService;

    private DealRequestDTO validDealDTO;
    private Deal validDealEntity;

    @BeforeEach
    void setUp() {
        // نوجدو داتا نقية قبل كل تيست
        validDealDTO = new DealRequestDTO(
                "DEAL-001", "USD", "JOD", Instant.now(), BigDecimal.valueOf(1000)
        );
        validDealEntity = new Deal();
        validDealEntity.setDealUniqueId("DEAL-001");
    }

    // 1. Happy Path: كلشي داز مزيان
    @Test
    @DisplayName("Should import valid deal successfully")
    void importDeals_Success() {
        // Mocking: ضروري نرجعو HashSet باش تكون قابلة للتعديل (Mutable) حيت عندك existingIds.add()
        when(dealRepository.findExistingIds(any())).thenReturn(new HashSet<>());
        when(validator.validate(any(DealRequestDTO.class))).thenReturn(Collections.emptySet());
        when(dealMapper.toEntity(any())).thenReturn(validDealEntity);

        ImportReportDTO report = dealService.importDeals(List.of(validDealDTO));

        assertEquals(1, report.successCount());
        assertEquals(0, report.failedDeals().size());
        assertEquals(0, report.duplicateCount());

        verify(dealRepository, times(1)).save(any(Deal.class));
    }

    // 2. Duplicate Check: الـ Deal ديجا كاين فـ DB
    @Test
    @DisplayName("Should detect duplicate if ID exists in DB")
    void importDeals_DuplicateInDB() {
        // Mocking: كنرجعو Set فيها الـ ID باش السيرفيس يعرفو كاين
        Set<String> existing = new HashSet<>();
        existing.add("DEAL-001");
        when(dealRepository.findExistingIds(any())).thenReturn(existing);

        ImportReportDTO report = dealService.importDeals(List.of(validDealDTO));

        assertEquals(0, report.successCount());
        assertEquals(1, report.duplicateCount());
        assertEquals(1, report.failedDeals().size());

        // التحقق من الميساج اللي درتي فـ الكود: "Deal exists"
        assertEquals("Deal exists", report.failedDeals().get(0).failureReason()); // أو .reason() حسب DTO ديالك

        verify(dealRepository, never()).save(any());
    }

    // 3. Validation Error: مشكل فـ الداتا (Validation Failed)
    @Test
    @DisplayName("Should fail if validation returns errors")
    void importDeals_ValidationError() {
        when(dealRepository.findExistingIds(any())).thenReturn(new HashSet<>());

        // Mocking Violation
        ConstraintViolation<DealRequestDTO> violation = mock(ConstraintViolation.class);
        when(violation.getMessage()).thenReturn("Invalid Currency");

        // استعملنا any(DealRequestDTO.class) باش نتفاداو مشكل الـ Generics
        when(validator.validate(any(DealRequestDTO.class))).thenReturn(Set.of(violation));

        ImportReportDTO report = dealService.importDeals(List.of(validDealDTO));

        assertEquals(0, report.successCount());
        assertEquals(1, report.failedDeals().size());
        assertEquals("Invalid Currency", report.failedDeals().get(0).failureReason());

        verify(dealRepository, never()).save(any());
    }

    // 4. Exception Handling: خطأ غير متوقع (مثلاً DB طاحت)
    @Test
    @DisplayName("Should handle runtime exceptions gracefully")
    void importDeals_RuntimeException() {
        when(dealRepository.findExistingIds(any())).thenReturn(new HashSet<>());
        when(validator.validate(any(DealRequestDTO.class))).thenReturn(Collections.emptySet());
        when(dealMapper.toEntity(any())).thenReturn(validDealEntity);

        // كنفرضو خطأ فـ Save
        doThrow(new RuntimeException("DB Connection Error")).when(dealRepository).save(any());

        ImportReportDTO report = dealService.importDeals(List.of(validDealDTO));

        assertEquals(0, report.successCount());
        assertEquals(1, report.failedDeals().size());
        assertTrue(report.failedDeals().get(0).failureReason().contains("DB Connection Error"));
    }

    // 5. Batch Logic: تكرار داخل نفس الملف (Smart Check)
    @Test
    @DisplayName("Should handle duplicates within the same batch")
    void importDeals_DuplicateInBatch() {
        // السيناريو: عندنا 2 صفقات بنفس الـ ID فـ نفس الليستة
        List<DealRequestDTO> batch = List.of(validDealDTO, validDealDTO);

        when(dealRepository.findExistingIds(any())).thenReturn(new HashSet<>()); // DB خاوية فـ الأول
        when(validator.validate(any(DealRequestDTO.class))).thenReturn(Collections.emptySet());
        when(dealMapper.toEntity(any())).thenReturn(validDealEntity);

        ImportReportDTO report = dealService.importDeals(batch);

        // النتيجة:
        // الأولى دازت وتزادت لـ existingIds (حيت درتي existingIds.add)
        // الثانية ترفضات حيت لقات راسها فـ existingIds
        assertEquals(1, report.successCount());
        assertEquals(1, report.duplicateCount());
        assertEquals(2, report.totalItems());
        assertEquals(1, report.failedDeals().size()); // الثانية فشلات

        verify(dealRepository, times(1)).save(any()); // Save تعيطات مرة وحدة
    }
}