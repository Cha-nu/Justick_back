package com.project.Justick.Service.Potato;

import com.project.Justick.Domain.Cabbage.CabbageRetail;
import com.project.Justick.Domain.Potato.PotatoRetail;
import com.project.Justick.Repository.Cabbage.CabbageRetailRepository;
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

        // 만약 데이터가 28개 이상이면 가장 오래된 1개 삭제
        if (all.size() >= 28) {
            PotatoRetail oldest = all.stream()
                    .min(Comparator.comparing(PotatoRetail::getYear)
                            .thenComparing(PotatoRetail::getMonth)
                            .thenComparing(PotatoRetail::getDay))
                    .orElse(null);

            if (oldest != null) {
                potatoRetailRepository.delete(oldest);
            }
        }

        return potatoRetailRepository.save(newData);
    }
}