package com.project.Justick.Service.Potato;

import com.project.Justick.DTO.Potato.PotatoPredictRequest;
import com.project.Justick.Domain.Grade;
import com.project.Justick.Domain.Potato.PotatoPredict;
import com.project.Justick.Repository.Potato.PotatoPredictRepository;
import com.project.Justick.Service.AgriculturalPredictService;
import org.springframework.stereotype.Service;


@Service
public class PotatoPredictService extends AgriculturalPredictService<PotatoPredict, PotatoPredictRequest> {

    public PotatoPredictService(PotatoPredictRepository repo) {
        super(repo);
    }

    @Override
    protected PotatoPredict toEntity(PotatoPredictRequest req) {
        PotatoPredict e = new PotatoPredict();
        e.setYear(req.getYear());
        e.setMonth(req.getMonth());
        e.setDay(req.getDay());
        e.setAveragePrice(req.getAveragePrice());
        e.setGrade(Grade.valueOf(req.getGrade().toUpperCase()));
        return e;
    }

    @Override
    protected String getGradeFromRequest(PotatoPredictRequest req) {
        return req.getGrade();
    }
}
