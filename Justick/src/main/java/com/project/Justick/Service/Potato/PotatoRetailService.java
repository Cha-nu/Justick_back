package com.project.Justick.Service.Potato;

import com.project.Justick.Domain.Potato.PotatoRetail;
import com.project.Justick.Repository.Potato.PotatoRetailRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class PotatoRetailService {

    private final PotatoRetailRepository potatoRetailRepository;

    public PotatoRetailService(PotatoRetailRepository potatoRetailRepository) {
        this.potatoRetailRepository = potatoRetailRepository;
    }

    public List<PotatoRetail> getAllPotatoRetail() {
        return potatoRetailRepository.findAll();
    }

    public PotatoRetail addPotatoRetail(PotatoRetail newData) {
        List<PotatoRetail> all = potatoRetailRepository.findAll();

        PotatoRetail prev = all.stream()
                .max(Comparator.comparing(PotatoRetail::getYear)
                        .thenComparing(PotatoRetail::getMonth)
                        .thenComparing(PotatoRetail::getDay))
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
            PotatoRetail oldest = all.stream()
                    .min(Comparator.comparing(PotatoRetail::getYear)
                            .thenComparing(PotatoRetail::getMonth)
                            .thenComparing(PotatoRetail::getDay))
                    .orElse(null);

            potatoRetailRepository.delete(oldest);
        }

        return potatoRetailRepository.save(newData);
    }
}