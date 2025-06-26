package com.project.Justick.Service.Radish;

import com.project.Justick.DTO.Radish.RadishRetailRequest;
import com.project.Justick.Domain.Radish.RadishRetail;
import com.project.Justick.Repository.Radish.RadishRetailRepository;
import com.project.Justick.Service.AgriculturalRetailService;
import org.springframework.stereotype.Service;

@Service
public class RadishRetailService extends AgriculturalRetailService<RadishRetail, RadishRetailRequest> {

    public RadishRetailService(RadishRetailRepository repo) {
        super(repo);
    }

    @Override
    protected RadishRetail toEntity(RadishRetailRequest req) {
        RadishRetail entity = new RadishRetail();
        entity.setYear(req.getYear());
        entity.setMonth(req.getMonth());
        entity.setDay(req.getDay());
        entity.setAveragePrice(req.getAveragePrice());
        entity.setGap(req.getGap());
        return entity;
    }

    public void addRadishRetail(RadishRetailRequest request) {
        saveWithLimit(request, 28);
    }
}