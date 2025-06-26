package com.project.Justick.Service.Onion;


import com.project.Justick.DTO.Onion.OnionRequest;
import com.project.Justick.Domain.Grade;
import com.project.Justick.Domain.Onion.Onion;
import com.project.Justick.Repository.Onion.OnionRepository;
import com.project.Justick.Service.AgriculturalService;
import org.springframework.stereotype.Service;


@Service
public class OnionService extends AgriculturalService<Onion, OnionRequest> {

    public OnionService(OnionRepository repository) {
        super(repository);
    }

    @Override
    protected Onion toEntity(OnionRequest request) {
        Onion c = new Onion();
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
