package com.project.Justick.Service.Tomato;

import com.project.Justick.DTO.Tomato.TomatoPredictRequest;
import com.project.Justick.Domain.Grade;
import com.project.Justick.Domain.Tomato.TomatoPredict;
import com.project.Justick.Repository.Tomato.TomatoPredictRepository;
import com.project.Justick.Service.AgriculturalPredictService;
import org.springframework.stereotype.Service;


@Service
public class TomatoPredictService extends AgriculturalPredictService<TomatoPredict, TomatoPredictRequest> {

    public TomatoPredictService(TomatoPredictRepository repo) {
        super(repo);
    }

    @Override
    protected TomatoPredict toEntity(TomatoPredictRequest req) {
        TomatoPredict e = new TomatoPredict();
        e.setYear(req.getYear());
        e.setMonth(req.getMonth());
        e.setDay(req.getDay());
        e.setAveragePrice(req.getAveragePrice());
        e.setGrade(Grade.valueOf(req.getGrade().toUpperCase()));
        return e;
    }

    @Override
    protected String getGradeFromRequest(TomatoPredictRequest req) {
        return req.getGrade();
    }
}