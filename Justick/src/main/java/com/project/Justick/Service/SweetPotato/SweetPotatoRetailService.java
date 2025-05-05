package com.project.Justick.Service.SweetPotato;

import com.project.Justick.Domain.Cabbage.CabbageRetail;
import com.project.Justick.Domain.SweetPotato.SweetPotatoRetail;
import com.project.Justick.Repository.Cabbage.CabbageRetailRepository;
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

        // 만약 데이터가 28개 이상이면 가장 오래된 1개 삭제
        if (all.size() >= 28) {
            SweetPotatoRetail oldest = all.stream()
                    .min(Comparator.comparing(SweetPotatoRetail::getYear)
                            .thenComparing(SweetPotatoRetail::getMonth)
                            .thenComparing(SweetPotatoRetail::getDay))
                    .orElse(null);

            if (oldest != null) {
                sweetPotatoRetailRepository.delete(oldest);
            }
        }

        return sweetPotatoRetailRepository.save(newData);
    }
}