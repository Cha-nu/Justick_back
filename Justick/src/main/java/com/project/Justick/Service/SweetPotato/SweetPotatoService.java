package com.project.Justick.Service.SweetPotato;

import com.project.Justick.DTO.SweetPotato.SweetPotatoRequest;
import com.project.Justick.Domain.Grade;
import com.project.Justick.Domain.SweetPotato.SweetPotato;
import com.project.Justick.Repository.SweetPotato.SweetPotatoRepository;
import com.project.Justick.Service.AgriculturalService;
import org.springframework.stereotype.Service;


@Service
public class SweetPotatoService extends AgriculturalService<SweetPotato, SweetPotatoRequest> {

    public SweetPotatoService(SweetPotatoRepository repository) {
        super(repository);
    }

    @Override
    protected SweetPotato toEntity(SweetPotatoRequest request) {
        SweetPotato c = new SweetPotato();
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
