package com.project.Justick.Service.Onion;

import com.project.Justick.DTO.Onion.OnionPredictRequest;
import com.project.Justick.Domain.Grade;
import com.project.Justick.Domain.Onion.OnionPredict;
import com.project.Justick.Repository.Onion.OnionPredictRepository;
import com.project.Justick.Service.AgriculturalPredictService;
import org.springframework.stereotype.Service;


@Service
public class OnionPredictService extends AgriculturalPredictService<OnionPredict, OnionPredictRequest> {

    public OnionPredictService(OnionPredictRepository repo) {
        super(repo);
    }

    @Override
    protected OnionPredict toEntity(OnionPredictRequest req) {
        OnionPredict e = new OnionPredict();
        e.setYear(req.getYear());
        e.setMonth(req.getMonth());
        e.setDay(req.getDay());
        e.setAveragePrice(req.getAveragePrice());
        e.setGrade(Grade.valueOf(req.getGrade().toUpperCase()));
        return e;
    }

    @Override
    protected String getGradeFromRequest(OnionPredictRequest req) {
        return req.getGrade();
    }
}
