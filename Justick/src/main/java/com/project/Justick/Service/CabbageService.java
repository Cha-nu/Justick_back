package com.project.Justick.Service;

import com.project.Justick.Domain.Cabbage;
import com.project.Justick.DTO.CabbageRequest;
import com.project.Justick.Domain.Grade;
import com.project.Justick.Domain.PredictPrice;
import com.project.Justick.Repository.CabbageRepository;
import com.project.Justick.Repository.PredictRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CabbageService {
    private final CabbageRepository repository;
    private final PredictRepository predictRepository;

    public CabbageService(CabbageRepository repository, PredictRepository predictRepository) {
        this.repository = repository;
        this.predictRepository = predictRepository;
    }

    public List<Cabbage> getHighPrices() {
        return repository.findByGrade(Grade.HIGH);
    }
    public List<Cabbage> getSpePrices() {
        return repository.findByGrade(Grade.SPECIAL);
    }

    public List<PredictPrice> getHighPredictPrice() {
        return predictRepository.findByNameAndGrade("cabbage", Grade.HIGH);
    }

    public List<PredictPrice> getSpePredictPrice() {
        return predictRepository.findByNameAndGrade("cabbage", Grade.SPECIAL);
    }

    public Cabbage save(CabbageRequest request) {
        Cabbage entity = new Cabbage();
        entity.setYear(request.getYear());
        entity.setMonth(request.getMonth());
        entity.setDay(request.getDay());
        entity.setAveragePrice(request.getAveragePrice());
        entity.setIntake(request.getIntake());
        entity.setGrade(Grade.valueOf(request.getGrade()));
        return repository.save(entity);
    }
}
