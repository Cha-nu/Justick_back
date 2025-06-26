package com.project.Justick.Service.SweetPotato;

import com.project.Justick.DTO.SweetPotato.SweetPotatoPredictRequest;
import com.project.Justick.Domain.Grade;
import com.project.Justick.Domain.SweetPotato.SweetPotatoPredict;
import com.project.Justick.Repository.SweetPotato.SweetPotatoPredictRepository;
import com.project.Justick.Service.AgriculturalPredictService;
import org.springframework.stereotype.Service;

@Service
public class SweetPotatoPredictService extends AgriculturalPredictService<SweetPotatoPredict, SweetPotatoPredictRequest> {

    public SweetPotatoPredictService(SweetPotatoPredictRepository repo) {
        super(repo);
    }

    @Override
    protected SweetPotatoPredict toEntity(SweetPotatoPredictRequest req) {
        SweetPotatoPredict e = new SweetPotatoPredict();
        e.setYear(req.getYear());
        e.setMonth(req.getMonth());
        e.setDay(req.getDay());
        e.setAveragePrice(req.getAveragePrice());
        e.setGrade(Grade.valueOf(req.getGrade().toUpperCase()));
        return e;
    }

    @Override
    protected String getGradeFromRequest(SweetPotatoPredictRequest req) {
        return req.getGrade();
    }
}
