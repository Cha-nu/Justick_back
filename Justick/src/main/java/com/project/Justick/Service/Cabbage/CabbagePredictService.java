package com.project.Justick.Service.Cabbage;

import com.project.Justick.DTO.Cabbage.CabbagePredictRequest;
import com.project.Justick.Domain.Cabbage.CabbagePredict;
import com.project.Justick.Domain.Grade;
import com.project.Justick.Repository.Cabbage.CabbagePredictRepository;
import com.project.Justick.Service.AgriculturalPredictService;
import org.springframework.stereotype.Service;


@Service
public class CabbagePredictService extends AgriculturalPredictService<CabbagePredict, CabbagePredictRequest> {

    public CabbagePredictService(CabbagePredictRepository repo) {
        super(repo);
    }

    @Override
    protected CabbagePredict toEntity(CabbagePredictRequest req) {
        CabbagePredict e = new CabbagePredict();
        e.setYear(req.getYear());
        e.setMonth(req.getMonth());
        e.setDay(req.getDay());
        e.setAveragePrice(req.getAveragePrice());
        e.setGrade(Grade.valueOf(req.getGrade().toUpperCase()));
        return e;
    }

    @Override
    protected String getGradeFromRequest(CabbagePredictRequest req) {
        return req.getGrade();
    }
}
