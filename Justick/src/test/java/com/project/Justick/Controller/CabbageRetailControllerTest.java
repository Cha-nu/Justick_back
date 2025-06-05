package com.project.Justick.Controller;

import com.project.Justick.Domain.Cabbage.CabbageRetail;
import com.project.Justick.Repository.Cabbage.CabbageRetailRepository;
import com.project.Justick.Service.Cabbage.CabbageRetailService;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CabbageRetailServiceTest {

    @Test
    void getAllCabbageRetail() {
        // given
        CabbageRetailRepository mockRepo = mock(CabbageRetailRepository.class);
        List<CabbageRetail> mockList = new ArrayList<>();
        CabbageRetail sample = new CabbageRetail();
        sample.setYear(2025);
        sample.setMonth(5);
        sample.setDay(5);
        sample.setAveragePrice(3000);
        mockList.add(sample);

        when(mockRepo.findAll()).thenReturn(mockList);
        CabbageRetailService service = new CabbageRetailService(mockRepo);

        // when
        List<CabbageRetail> result = service.getAllCabbageRetail();

        // then
        assertEquals(1, result.size());
        assertEquals(2025, result.get(0).getYear());
    }

    @Test
    void createCabbageRetail_limitedTo28Items() {
        // given
        CabbageRetailRepository mockRepo = mock(CabbageRetailRepository.class);

        List<CabbageRetail> existing = new ArrayList<>();
        for (int i = 0; i < 28; i++) {
            CabbageRetail item = new CabbageRetail();
            item.setId((long) i);
            item.setYear(2024);
            item.setMonth(1);
            item.setDay(i + 1);
            existing.add(item);
        }

        when(mockRepo.findAll()).thenReturn(existing);
        doNothing().when(mockRepo).delete(any());
        when(mockRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        CabbageRetailService service = new CabbageRetailService(mockRepo);

        // 새로 추가할 데이터
        CabbageRetail newData = new CabbageRetail();
        newData.setYear(2025);
        newData.setMonth(5);
        newData.setDay(5);
        newData.setAveragePrice(3000);

        // when
        CabbageRetail saved = service.addCabbageRetail(newData);

        // then
        verify(mockRepo, times(1)).delete(any()); // 가장 오래된 데이터 삭제
        verify(mockRepo, times(1)).save(newData); // 새로운 데이터 저장
        assertEquals(2025, saved.getYear());
    }
}
