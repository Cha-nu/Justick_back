package com.project.Justick.Service.Tomato;


import com.project.Justick.Domain.Tomato.TomatoRetail;
import com.project.Justick.Repository.Tomato.TomatoRetailRepository;
import org.springframework.stereotype.Service;

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

    public TomatoRetail addTomatoRetail(TomatoRetail newData) {
        List<TomatoRetail> all = tomatoRetailRepository.findAll();

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

        return tomatoRetailRepository.save(newData);
    }
}