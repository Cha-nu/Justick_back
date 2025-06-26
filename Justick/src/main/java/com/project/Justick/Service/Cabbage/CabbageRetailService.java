package com.project.Justick.Service.Cabbage;

import com.project.Justick.DTO.Cabbage.CabbageRetailRequest;
import com.project.Justick.Domain.Cabbage.CabbageRetail;
import com.project.Justick.Repository.Cabbage.CabbageRetailRepository;
import com.project.Justick.Service.AgriculturalRetailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class CabbageRetailService extends AgriculturalRetailService<CabbageRetail, CabbageRetailRequest> {

    public CabbageRetailService(CabbageRetailRepository repo) {
        super(repo);
    }

    @Override
    protected CabbageRetail toEntity(CabbageRetailRequest req) {
        CabbageRetail entity = new CabbageRetail();
        entity.setYear(req.getYear());
        entity.setMonth(req.getMonth());
        entity.setDay(req.getDay());
        entity.setAveragePrice(req.getAveragePrice());
        entity.setGap(req.getGap());
        return entity;
    }

    public void addCabbageRetail(CabbageRetailRequest request) {
        saveWithLimit(request, 28);
    }
}