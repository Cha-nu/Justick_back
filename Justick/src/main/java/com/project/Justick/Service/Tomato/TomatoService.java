package com.project.Justick.Service.Tomato;

import com.project.Justick.DTO.Tomato.TomatoRequest;
import com.project.Justick.Domain.Grade;
import com.project.Justick.Domain.Tomato.Tomato;
import com.project.Justick.Repository.Tomato.TomatoRepository;
import com.project.Justick.Service.AgriculturalService;
import org.springframework.stereotype.Service;


@Service
public class TomatoService extends AgriculturalService<Tomato, TomatoRequest> {

    public TomatoService(TomatoRepository repository) {
        super(repository);
    }

    @Override
    protected Tomato toEntity(TomatoRequest request) {
        Tomato c = new Tomato();
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
