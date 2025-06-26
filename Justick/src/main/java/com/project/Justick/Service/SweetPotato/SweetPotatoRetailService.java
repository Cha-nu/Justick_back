package com.project.Justick.Service.SweetPotato;

import com.project.Justick.DTO.Radish.RadishRetailRequest;
import com.project.Justick.DTO.SweetPotato.SweetPotatoRetailRequest;
import com.project.Justick.Domain.Radish.RadishRetail;
import com.project.Justick.Domain.SweetPotato.SweetPotatoRetail;
import com.project.Justick.Repository.Radish.RadishRetailRepository;
import com.project.Justick.Repository.SweetPotato.SweetPotatoRetailRepository;
import com.project.Justick.Service.AgriculturalRetailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class SweetPotatoRetailService extends AgriculturalRetailService<SweetPotatoRetail, SweetPotatoRetailRequest> {

    public SweetPotatoRetailService(SweetPotatoRetailRepository repo) {
        super(repo);
    }

    @Override
    protected SweetPotatoRetail toEntity(SweetPotatoRetailRequest req) {
        SweetPotatoRetail entity = new SweetPotatoRetail();
        entity.setYear(req.getYear());
        entity.setMonth(req.getMonth());
        entity.setDay(req.getDay());
        entity.setAveragePrice(req.getAveragePrice());
        entity.setGap(req.getGap());
        return entity;
    }

    public void addSweetPotatoRetail(SweetPotatoRetailRequest request) {
        saveWithLimit(request, 28);
    }
}