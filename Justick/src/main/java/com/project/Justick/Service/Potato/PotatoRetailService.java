package com.project.Justick.Service.Potato;

import com.project.Justick.DTO.Potato.PotatoRetailRequest;
import com.project.Justick.Domain.Potato.PotatoRetail;
import com.project.Justick.Repository.Potato.PotatoRetailRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public void addPotatoRetail(PotatoRetailRequest request) {
        PotatoRetail newData = toEntity(request);
        List<PotatoRetail> all = potatoRetailRepository.findAll();

        // 만약 데이터가 28개 이상이면 가장 오래된 1개 삭제
        if (all.size() >= 28) {
            PotatoRetail oldest = all.stream()
                    .min(Comparator.comparing(PotatoRetail::getYear)
                            .thenComparing(PotatoRetail::getMonth)
                            .thenComparing(PotatoRetail::getDay))
                    .orElse(null);

            potatoRetailRepository.delete(oldest);
        }

        potatoRetailRepository.save(newData);
    }

    private PotatoRetail toEntity(PotatoRetailRequest request) {
        PotatoRetail entity = new PotatoRetail();
        entity.setYear(request.getYear());
        entity.setMonth(request.getMonth());
        entity.setDay(request.getDay());
        entity.setAveragePrice(request.getAveragePrice());
        entity.setGap(request.getGap());
        // 필요시 추가 필드 매핑
        return entity;
    }

    @Transactional
    public void saveAll(List<PotatoRetailRequest> requests) {
        List<PotatoRetail> list = requests.stream().map(this::toEntity).toList();
        potatoRetailRepository.saveAll(list);
    }

    @Transactional
    public void deleteById(Long id) {
        potatoRetailRepository.deleteById(id);
    }
}