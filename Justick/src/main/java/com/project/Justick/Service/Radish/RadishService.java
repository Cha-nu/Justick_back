package com.project.Justick.Service.Radish;

import com.project.Justick.DTO.Radish.RadishRequest;
import com.project.Justick.Domain.Grade;
import com.project.Justick.Domain.Radish.Radish;
import com.project.Justick.Repository.Radish.RadishRepository;
import com.project.Justick.Service.AgriculturalService;
import org.springframework.stereotype.Service;



@Service
public class RadishService extends AgriculturalService<Radish, RadishRequest> {

    public RadishService(RadishRepository repository) {
        super(repository);
    }

    @Override
    protected Radish toEntity(RadishRequest request) {
        Radish c = new Radish();
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
