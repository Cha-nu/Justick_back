package com.project.Justick.Service.SweetPotato;

import com.project.Justick.DTO.SweetPotato.SweetPotatoRetailRequest;
import com.project.Justick.Domain.SweetPotato.SweetPotatoRetail;
import com.project.Justick.Repository.SweetPotato.SweetPotatoRetailRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public void addSweetPotatoRetail(SweetPotatoRetailRequest request) {
        SweetPotatoRetail newData = toEntity(request);
        List<SweetPotatoRetail> all = sweetPotatoRetailRepository.findAll();
        // 만약 데이터가 28개 이상이면 가장 오래된 1개 삭제
        if (all.size() >= 28) {
            SweetPotatoRetail oldest = all.stream()
                    .min(Comparator.comparing(SweetPotatoRetail::getYear)
                            .thenComparing(SweetPotatoRetail::getMonth)
                            .thenComparing(SweetPotatoRetail::getDay))
                    .orElse(null);

            sweetPotatoRetailRepository.delete(oldest);
        }

        sweetPotatoRetailRepository.save(newData);
    }

    private SweetPotatoRetail toEntity(SweetPotatoRetailRequest request) {
        SweetPotatoRetail entity = new SweetPotatoRetail();
        entity.setYear(request.getYear());
        entity.setMonth(request.getMonth());
        entity.setDay(request.getDay());
        entity.setAveragePrice(request.getAveragePrice());
        entity.setGap(request.getGap());
        // 필요시 추가 필드 매핑
        return entity;
    }

    @Transactional
    public void saveAll(List<SweetPotatoRetailRequest> requests) {
        List<SweetPotatoRetail> list = requests.stream().map(this::toEntity).toList();
        sweetPotatoRetailRepository.saveAll(list);
    }

    @Transactional
    public void deleteById(Long id) {
        sweetPotatoRetailRepository.deleteById(id);
    }
}