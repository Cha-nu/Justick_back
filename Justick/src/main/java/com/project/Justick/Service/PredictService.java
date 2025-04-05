package com.project.Justick.Service;

import com.project.Justick.DTO.PredictRequest;
import com.project.Justick.Domain.Category;
import com.project.Justick.Domain.Grade;
import com.project.Justick.Domain.PredictPrice;
import com.project.Justick.Repository.PredictRepository;
import org.springframework.stereotype.Service;

@Service
public class PredictService {

    private final PredictRepository predictRepository;

    public PredictService(PredictRepository predictRepository) {
        // Constructor
        this.predictRepository = predictRepository;
    }

    // 저장 메서드
    public PredictPrice save(PredictRequest request) {
        PredictPrice entity = new PredictPrice();
        entity.setName(request.getName());
        entity.setCategory(Category.valueOf(request.getCategory()));
        entity.setPredictPrice(request.getPredictPrice());
        entity.setGrade(Grade.valueOf(request.getGrade()));
        return predictRepository.save(entity);
    }
    // 수정 메서드
    public PredictPrice update(PredictPrice entity, PredictRequest request) {
        entity.setName(request.getName());
        entity.setCategory(Category.valueOf(request.getCategory()));
        entity.setPredictPrice(request.getPredictPrice());
        return entity;
    }
}
