package com.project.Justick.Service.Tomato;



import com.project.Justick.DTO.Radish.RadishRetailRequest;
import com.project.Justick.DTO.Tomato.TomatoRetailRequest;
import com.project.Justick.Domain.Radish.RadishRetail;
import com.project.Justick.Domain.Tomato.TomatoRetail;
import com.project.Justick.Repository.Radish.RadishRetailRepository;
import com.project.Justick.Repository.Tomato.TomatoRetailRepository;
import com.project.Justick.Service.AgriculturalRetailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class TomatoRetailService extends AgriculturalRetailService<TomatoRetail, TomatoRetailRequest> {

    public TomatoRetailService(TomatoRetailRepository repo) {
        super(repo);
    }

    @Override
    protected TomatoRetail toEntity(TomatoRetailRequest req) {
        TomatoRetail entity = new TomatoRetail();
        entity.setYear(req.getYear());
        entity.setMonth(req.getMonth());
        entity.setDay(req.getDay());
        entity.setAveragePrice(req.getAveragePrice());
        entity.setGap(req.getGap());
        return entity;
    }

    public void addTomatoRetail(TomatoRetailRequest request) {
        saveWithLimit(request, 28);
    }
}