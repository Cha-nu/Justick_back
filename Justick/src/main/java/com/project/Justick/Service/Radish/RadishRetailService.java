package com.project.Justick.Service.Radish;

import com.project.Justick.Domain.Radish.RadishRetail;
import com.project.Justick.Repository.Radish.RadishRetailRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class RadishRetailService {

    private final RadishRetailRepository radishRetailRepository;

    public RadishRetailService(RadishRetailRepository radishRetailRepository) {
        this.radishRetailRepository = radishRetailRepository;
    }

    public List<RadishRetail> getAllRadishRetail() {
        return radishRetailRepository.findAll();
    }

    public RadishRetail addRadishRetail(RadishRetail newData) {
        List<RadishRetail> all = radishRetailRepository.findAll();

        RadishRetail prev = all.stream()
                .max(Comparator.comparing(RadishRetail::getYear)
                        .thenComparing(RadishRetail::getMonth)
                        .thenComparing(RadishRetail::getDay))
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
            RadishRetail oldest = all.stream()
                    .min(Comparator.comparing(RadishRetail::getYear)
                            .thenComparing(RadishRetail::getMonth)
                            .thenComparing(RadishRetail::getDay))
                    .orElse(null);

            radishRetailRepository.delete(oldest);
        }

        return radishRetailRepository.save(newData);
    }
}