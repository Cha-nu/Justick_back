package com.project.Justick.Service.Cabbage;

import com.project.Justick.Domain.Cabbage.CabbageRetail;
import com.project.Justick.Repository.Cabbage.CabbageRetailRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class CabbageRetailService {

    private final CabbageRetailRepository cabbageRetailRepository;

    public CabbageRetailService(CabbageRetailRepository cabbageRetailRepository) {
        this.cabbageRetailRepository = cabbageRetailRepository;
    }

    public List<CabbageRetail> getAllCabbageRetail() {
        return cabbageRetailRepository.findAll();
    }

    public CabbageRetail addCabbageRetail(CabbageRetail newData) {
        List<CabbageRetail> all = cabbageRetailRepository.findAll();

        // 만약 데이터가 28개 이상이면 가장 오래된 1개 삭제
        if (all.size() >= 28) {
            CabbageRetail oldest = all.stream()
                    .min(Comparator.comparing(CabbageRetail::getYear)
                            .thenComparing(CabbageRetail::getMonth)
                            .thenComparing(CabbageRetail::getDay))
                    .orElse(null);

            if (oldest != null) {
                cabbageRetailRepository.delete(oldest);
            }
        }

        return cabbageRetailRepository.save(newData);
    }
}