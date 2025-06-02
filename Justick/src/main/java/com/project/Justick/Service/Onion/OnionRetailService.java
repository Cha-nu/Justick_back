package com.project.Justick.Service.Onion;

import com.project.Justick.DTO.Onion.OnionRetailRequest;
import com.project.Justick.Domain.Onion.OnionRetail;
import com.project.Justick.Repository.Onion.OnionRetailRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public OnionRetail addOnionRetail(OnionRetailRequest request) {
        OnionRetail newData = toEntity(request);
        List<OnionRetail> all = onionRetailRepository.findAll();

        // 만약 데이터가 28개 이상이면 가장 오래된 1개 삭제
        if (all.size() >= 28) {
            OnionRetail oldest = all.stream()
                    .min(Comparator.comparing(OnionRetail::getYear)
                            .thenComparing(OnionRetail::getMonth)
                            .thenComparing(OnionRetail::getDay))
                    .orElse(null);

            onionRetailRepository.delete(oldest);
        }

        return onionRetailRepository.save(newData);
    }

    private OnionRetail toEntity(OnionRetailRequest request) {
        OnionRetail entity = new OnionRetail();
        entity.setYear(request.getYear());
        entity.setMonth(request.getMonth());
        entity.setDay(request.getDay());
        entity.setAveragePrice(request.getAveragePrice());
        entity.setGap(request.getGap());
        // 필요시 추가 필드 매핑
        return entity;
    }

    @Transactional
    public void saveAll(List<OnionRetailRequest> requests) {
        List<OnionRetail> list = requests.stream().map(this::toEntity).toList();
        onionRetailRepository.saveAll(list);
    }

    @Transactional
    public void deleteById(Long id) {
        onionRetailRepository.deleteById(id);
    }
}