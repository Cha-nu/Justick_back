package com.project.Justick.Service.Onion;

import com.project.Justick.DTO.Onion.OnionRetailRequest;
import com.project.Justick.Domain.Onion.OnionRetail;
import com.project.Justick.Repository.Onion.OnionRetailRepository;
import com.project.Justick.Service.AgriculturalRetailService;
import org.springframework.stereotype.Service;

@Service
public class OnionRetailService extends AgriculturalRetailService<OnionRetail, OnionRetailRequest> {

    public OnionRetailService(OnionRetailRepository repo) {
        super(repo);
    }

    @Override
    protected OnionRetail toEntity(OnionRetailRequest req) {
        OnionRetail entity = new OnionRetail();
        entity.setYear(req.getYear());
        entity.setMonth(req.getMonth());
        entity.setDay(req.getDay());
        entity.setAveragePrice(req.getAveragePrice());
        entity.setGap(req.getGap());
        return entity;
    }

    public void addOnionRetail(OnionRetailRequest request) {
        saveWithLimit(request, 28);
    }
}