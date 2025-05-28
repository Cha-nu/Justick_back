package com.project.Justick.Service;

import com.project.Justick.DTO.Cabbage.CabbageRequest;
import com.project.Justick.Domain.Cabbage.Cabbage;
import com.project.Justick.Domain.Grade;
import com.project.Justick.Repository.Cabbage.CabbageRepository;
import com.project.Justick.Service.Cabbage.CabbageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CabbageServiceTest {

    private CabbageRepository repository;
    private CabbageService service;

    @BeforeEach
    void setUp() {
        repository = mock(CabbageRepository.class);
        service = new CabbageService(repository); // PredictRepository는 안 씀
    }

    @Test
    @DisplayName("주간 평균을 구한다. (주차 1~8)")
    void testGetWeeklyAverages_returnsOnlyWeeks1to8() {
        // given
        List<Cabbage> mockData = new ArrayList<>();
        mockData.add(createCabbage(2024, 1, 2, 1000, 500));  // 1주차
        mockData.add(createCabbage(2024, 1, 5, 1200, 600));  // 1~2주차
        mockData.add(createCabbage(2024, 1, 15, 1300, 700)); // 3주차
        mockData.add(createCabbage(2024, 1, 29, 1500, 800)); // 5주차
        mockData.add(createCabbage(2024, 2, 28, 2000, 1000)); // 8주차
        mockData.add(createCabbage(2024, 3, 31, 2300, 1200)); // 9주차 → 제외
        mockData.add(createCabbage(2024, 4, 15, 1800, 1100)); // 7주차

        when(repository.findByGrade(Grade.HIGH)).thenReturn(mockData);

        // when
        Map<String, Map<String, Integer>> result = service.getWeeklyAverages(Grade.HIGH);

        // then
        assertEquals(6, result.size()); // 9주차는 빠짐
        for (String key : result.keySet()) {
            int week = Integer.parseInt(key.substring(key.length() - 2));
            assertTrue(week <= 8, "8주차 초과 있음: " + key);
        }
        result.keySet().forEach(System.out::println);

        Map<String, Integer> week01 = result.get("2024-01-01");
        assertNotNull(week01);
        assertEquals(1100, week01.get("averagePrice")); // (1000+1200)/2
        assertEquals(550, week01.get("intake"));        // (500+600)/2
    }

    @Test
    @DisplayName("월간 평균을 구한다.")
    void getMonthlyAverages() {
        // given
        List<Cabbage> data = Arrays.asList(
                createCabbage(2024, 1, 2, 1000, 500),
                createCabbage(2024, 2, 5, 1200, 600)
        );
        // when
        when(repository.findByGrade(Grade.HIGH)).thenReturn(data);

        Map<String, Map<String, Integer>> result = service.getMonthlyAverages(Grade.HIGH);

        // then
        assertEquals(2, result.size());
        assertTrue(result.keySet().contains("2024-01"));
        assertTrue(result.keySet().contains("2024-02"));
    }

    @Test
    @DisplayName("저장할 데이터가 28개 미만일 때는 삭제하지 않고 저장한다.")
    void saveAll() {
        // given
        CabbageRequest req = new CabbageRequest();
        req.setYear(2024);
        req.setMonth(1);
        req.setDay(2);
        req.setAveragePrice(1000);
        req.setIntake(500);
        req.setGrade("HIGH");

        // when
        when(repository.countByGrade(Grade.HIGH)).thenReturn(28L);
        when(repository.findByGradeAndYearAndMonthAndDay(any(), anyInt(), anyInt(), anyInt()))
                .thenReturn(Optional.empty());

        service.saveAll(List.of(req));
        // then
        verify(repository, times(1)).saveAll(anyList());
    }

    @Test
    @DisplayName("저장할 데이터가 28개 이상일 때는 가장 오래된 데이터를 삭제하고 저장한다.")
    void saveOneAndDeleteOldest_28() {
        // given
        CabbageRequest req = new CabbageRequest();
        req.setYear(2024);
        req.setMonth(1);
        req.setDay(2);
        req.setAveragePrice(1000);
        req.setIntake(500);
        req.setGrade("HIGH");

        // when
        when(repository.countByGrade(Grade.HIGH)).thenReturn(28L);
        when(repository.findByGradeAndYearAndMonthAndDay(any(), anyInt(), anyInt(), anyInt()))
                .thenReturn(Optional.empty());

        service.saveOneAndDeleteOldest(req);
        // then
        verify(repository, times(1)).deleteOldestByGrade(Grade.HIGH);
        verify(repository, times(1)).save(any(Cabbage.class));
    }

    private Cabbage createCabbage(int year, int month, int day, int avgPrice, int intake) {
        Cabbage c = new Cabbage();
        c.setYear(year);
        c.setMonth(month);
        c.setDay(day);
        c.setAveragePrice(avgPrice);
        c.setIntake(intake);
        c.setGrade(Grade.HIGH);
        return c;
    }
}
