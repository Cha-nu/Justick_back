package com.project.Justick.Service.SweetPotato;

import com.project.Justick.Domain.SweetPotato.SweetPotatoRetail;
import com.project.Justick.Repository.SweetPotato.SweetPotatoRetailRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class SweetPotatoRetailService {

    private final SweetPotatoRetailRepository sweetPotatoRetailRepository;

    public SweetPotatoRetailService(SweetPotatoRetailRepository sweetPotatoRetailRepository) {
        this.sweetPotatoRetailRepository = sweetPotatoRetailRepository;
    }

    public List<SweetPotatoRetail> getAllSweetPotatoRetail() {
        return sweetPotatoRetailRepository.findAll();
    }

    public SweetPotatoRetail addSweetPotatoRetail(SweetPotatoRetail newData) {
        List<SweetPotatoRetail> all = sweetPotatoRetailRepository.findAll();

        SweetPotatoRetail prev = all.stream()
                .max(Comparator.comparing(SweetPotatoRetail::getYear)
                        .thenComparing(SweetPotatoRetail::getMonth)
                        .thenComparing(SweetPotatoRetail::getDay))
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
            SweetPotatoRetail oldest = all.stream()
                    .min(Comparator.comparing(SweetPotatoRetail::getYear)
                            .thenComparing(SweetPotatoRetail::getMonth)
                            .thenComparing(SweetPotatoRetail::getDay))
                    .orElse(null);

            sweetPotatoRetailRepository.delete(oldest);
        }

        return sweetPotatoRetailRepository.save(newData);
    }
}