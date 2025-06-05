package com.project.Justick.Service.Radish;

import com.project.Justick.DTO.Radish.RadishRetailRequest;
import com.project.Justick.Domain.Radish.RadishRetail;
import com.project.Justick.Repository.Radish.RadishRetailRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public void addRadishRetail(RadishRetailRequest request) {
        RadishRetail newData = toEntity(request);
        List<RadishRetail> all = radishRetailRepository.findAll();

        // 만약 데이터가 28개 이상이면 가장 오래된 1개 삭제
        if (all.size() >= 28) {
            RadishRetail oldest = all.stream()
                    .min(Comparator.comparing(RadishRetail::getYear)
                            .thenComparing(RadishRetail::getMonth)
                            .thenComparing(RadishRetail::getDay))
                    .orElse(null);

            radishRetailRepository.delete(oldest);
        }

        radishRetailRepository.save(newData);
    }

    private RadishRetail toEntity(RadishRetailRequest request) {
        RadishRetail entity = new RadishRetail();
        entity.setYear(request.getYear());
        entity.setMonth(request.getMonth());
        entity.setDay(request.getDay());
        entity.setAveragePrice(request.getAveragePrice());
        entity.setGap(request.getGap());
        // 필요시 추가 필드 매핑
        return entity;
    }

    @Transactional
    public void saveAll(List<RadishRetailRequest> requests) {
        List<RadishRetail> list = requests.stream().map(this::toEntity).toList();
        radishRetailRepository.saveAll(list);
    }

    @Transactional
    public void deleteById(Long id) {
        radishRetailRepository.deleteById(id);
    }
}