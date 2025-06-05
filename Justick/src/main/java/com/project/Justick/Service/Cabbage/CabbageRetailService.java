package com.project.Justick.Service.Cabbage;

import com.project.Justick.DTO.Cabbage.CabbageRetailRequest;
import com.project.Justick.Domain.Cabbage.CabbageRetail;
import com.project.Justick.Repository.Cabbage.CabbageRetailRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public void addCabbageRetail(CabbageRetailRequest request) {
        CabbageRetail newData = toEntity(request);
        List<CabbageRetail> all = cabbageRetailRepository.findAll();

        // 만약 데이터가 28개 이상이면 가장 오래된 1개 삭제
        if (all.size() >= 28) {
            CabbageRetail oldest = all.stream()
                    .min(Comparator.comparing(CabbageRetail::getYear)
                            .thenComparing(CabbageRetail::getMonth)
                            .thenComparing(CabbageRetail::getDay))
                    .orElse(null);

            cabbageRetailRepository.delete(oldest);
        }

        cabbageRetailRepository.save(newData);
    }

    private CabbageRetail toEntity(CabbageRetailRequest request) {
        CabbageRetail entity = new CabbageRetail();
        entity.setYear(request.getYear());
        entity.setMonth(request.getMonth());
        entity.setDay(request.getDay());
        entity.setAveragePrice(request.getAveragePrice());
        entity.setGap(request.getGap());
        // 필요시 추가 필드 매핑
        return entity;
    }

    @Transactional
    public void saveAll(List<CabbageRetailRequest> requests) {
        List<CabbageRetail> list = requests.stream().map(this::toEntity).toList();
        cabbageRetailRepository.saveAll(list);
    }

    @Transactional
    public void deleteById(Long id) {
        cabbageRetailRepository.deleteById(id);
    }
}