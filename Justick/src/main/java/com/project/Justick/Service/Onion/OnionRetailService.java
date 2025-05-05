package com.project.Justick.Service.Onion;

import com.project.Justick.Domain.Onion.OnionRetail;
import com.project.Justick.Repository.Onion.OnionRetailRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class OnionRetailService {

    private final OnionRetailRepository onionRetailRepository;

    public OnionRetailService(OnionRetailRepository onionRetailRepository) {
        this.onionRetailRepository = onionRetailRepository;
    }

    public List<OnionRetail> getAllOnionRetail() {
        return onionRetailRepository.findAll();
    }

    public OnionRetail addOnionRetail(OnionRetail newData) {
        List<OnionRetail> all = onionRetailRepository.findAll();

        // 만약 데이터가 28개 이상이면 가장 오래된 1개 삭제
        if (all.size() >= 28) {
            OnionRetail oldest = all.stream()
                    .min(Comparator.comparing(OnionRetail::getYear)
                            .thenComparing(OnionRetail::getMonth)
                            .thenComparing(OnionRetail::getDay))
                    .orElse(null);

            if (oldest != null) {
                onionRetailRepository.delete(oldest);
            }
        }

        return onionRetailRepository.save(newData);
    }
}