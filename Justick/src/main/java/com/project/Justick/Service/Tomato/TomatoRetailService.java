package com.project.Justick.Service.Tomato;



import com.project.Justick.DTO.Tomato.TomatoRetailRequest;
import com.project.Justick.Domain.Tomato.TomatoRetail;
import com.project.Justick.Repository.Tomato.TomatoRetailRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class TomatoRetailService {

    private final TomatoRetailRepository tomatoRetailRepository;

    public TomatoRetailService(TomatoRetailRepository tomatoRetailRepository) {
        this.tomatoRetailRepository = tomatoRetailRepository;
    }

    public List<TomatoRetail> getAllTomatoRetail() {
        return tomatoRetailRepository.findAll();
    }

    public void addTomatoRetail(TomatoRetailRequest request) {
        TomatoRetail newData = toEntity(request);
        List<TomatoRetail> all = tomatoRetailRepository.findAll();

        // 가장 최근 데이터 찾기 (날짜 기준)
        TomatoRetail prev = all.stream()
                .max(Comparator.comparing(TomatoRetail::getYear)
                        .thenComparing(TomatoRetail::getMonth)
                        .thenComparing(TomatoRetail::getDay))
                .orElse(null);

        // gap 계산
        if (prev != null) {
            int gap = newData.getAveragePrice() - prev.getAveragePrice();
            newData.setGap(gap);
        } else {
            newData.setGap(0); // 첫 데이터는 gap 0
        }

        // 만약 데이터가 28개 이상이면 가장 오래된 1개 삭제
        if (all.size() >= 28) {
            TomatoRetail oldest = all.stream()
                    .min(Comparator.comparing(TomatoRetail::getYear)
                            .thenComparing(TomatoRetail::getMonth)
                            .thenComparing(TomatoRetail::getDay))
                    .orElse(null);

            tomatoRetailRepository.delete(oldest);
        }

        tomatoRetailRepository.save(newData);
    }

    private TomatoRetail toEntity(TomatoRetailRequest request) {
        TomatoRetail entity = new TomatoRetail();
        entity.setYear(request.getYear());
        entity.setMonth(request.getMonth());
        entity.setDay(request.getDay());
        entity.setAveragePrice(request.getAveragePrice());
        entity.setGap(request.getGap());
        // 필요시 추가 필드 매핑
        return entity;
    }

    @Transactional
    public void saveAll(List<TomatoRetailRequest> requests) {
        List<TomatoRetail> list = requests.stream().map(this::toEntity).toList();
        tomatoRetailRepository.saveAll(list);
    }

    @Transactional
    public void deleteById(Long id) {
        tomatoRetailRepository.deleteById(id);
    }
}