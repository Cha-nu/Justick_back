package com.project.Justick.Service.Cabbage;

import com.project.Justick.Domain.Cabbage.Cabbage;
import com.project.Justick.DTO.Cabbage.CabbageRequest;
import com.project.Justick.Domain.Grade;
import com.project.Justick.Repository.Cabbage.CabbageRepository;
import com.project.Justick.Service.AgriculturalService;
import org.springframework.stereotype.Service;



@Service
public class CabbageService extends AgriculturalService<Cabbage, CabbageRequest> {

    public CabbageService(CabbageRepository repository) {
        super(repository);
    }

    @Override
    protected Cabbage toEntity(CabbageRequest request) {
        Cabbage c = new Cabbage();
        c.setYear(request.getYear());
        c.setMonth(request.getMonth());
        c.setDay(request.getDay());
        c.setAveragePrice(request.getAveragePrice());
        c.setIntake(request.getIntake());
        c.setGap(request.getGap());
        c.setGrade(Grade.valueOf(request.getGrade()));
        return c;
    }
}