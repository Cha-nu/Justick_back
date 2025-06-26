package com.project.Justick.Service.Radish;

import com.project.Justick.DTO.Radish.RadishPredictRequest;
import com.project.Justick.Domain.Grade;
import com.project.Justick.Domain.Radish.RadishPredict;
import com.project.Justick.Repository.Radish.RadishPredictRepository;
import com.project.Justick.Service.AgriculturalPredictService;
import org.springframework.stereotype.Service;

@Service
public class RadishPredictService extends AgriculturalPredictService<RadishPredict, RadishPredictRequest> {

    public RadishPredictService(RadishPredictRepository repo) {
        super(repo);
    }

    @Override
    protected RadishPredict toEntity(RadishPredictRequest req) {
        RadishPredict e = new RadishPredict();
        e.setYear(req.getYear());
        e.setMonth(req.getMonth());
        e.setDay(req.getDay());
        e.setAveragePrice(req.getAveragePrice());
        e.setGrade(Grade.valueOf(req.getGrade().toUpperCase()));
        return e;
    }

    @Override
    protected String getGradeFromRequest(RadishPredictRequest req) {
        return req.getGrade();
    }
}