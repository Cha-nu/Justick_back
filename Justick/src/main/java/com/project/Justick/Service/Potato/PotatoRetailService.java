package com.project.Justick.Service.Potato;

import com.project.Justick.DTO.Potato.PotatoRetailRequest;
import com.project.Justick.Domain.Potato.PotatoRetail;
import com.project.Justick.Repository.Potato.PotatoRetailRepository;
import com.project.Justick.Service.AgriculturalRetailService;
import org.springframework.stereotype.Service;

@Service
public class PotatoRetailService extends AgriculturalRetailService<PotatoRetail, PotatoRetailRequest> {

    public PotatoRetailService(PotatoRetailRepository repo) {
        super(repo);
    }

    @Override
    protected PotatoRetail toEntity(PotatoRetailRequest req) {
        PotatoRetail entity = new PotatoRetail();
        entity.setYear(req.getYear());
        entity.setMonth(req.getMonth());
        entity.setDay(req.getDay());
        entity.setAveragePrice(req.getAveragePrice());
        entity.setGap(req.getGap());
        return entity;
    }

    public void addPotatoRetail(PotatoRetailRequest request) {
        saveWithLimit(request, 28);
    }
}