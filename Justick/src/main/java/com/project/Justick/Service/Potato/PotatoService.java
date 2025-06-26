package com.project.Justick.Service.Potato;

import com.project.Justick.DTO.Potato.PotatoRequest;
import com.project.Justick.Domain.Grade;
import com.project.Justick.Domain.Potato.Potato;
import com.project.Justick.Repository.Potato.PotatoRepository;
import com.project.Justick.Service.AgriculturalService;
import org.springframework.stereotype.Service;

@Service
public class PotatoService extends AgriculturalService<Potato, PotatoRequest> {

    public PotatoService(PotatoRepository repository) {
        super(repository);
    }

    @Override
    protected Potato toEntity(PotatoRequest request) {
        Potato c = new Potato();
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
