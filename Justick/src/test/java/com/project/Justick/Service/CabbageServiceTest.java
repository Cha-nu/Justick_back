package com.project.Justick.Service;

import com.project.Justick.Domain.Cabbage.Cabbage;
import com.project.Justick.Domain.Grade;
import com.project.Justick.Repository.Cabbage.CabbageRepository;
import com.project.Justick.Service.Cabbage.CabbageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
