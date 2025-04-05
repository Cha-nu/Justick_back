package com.project.Justick.Repository;

import com.project.Justick.Domain.Grade;
import com.project.Justick.Domain.PredictPrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PredictRepository extends JpaRepository<PredictPrice, Long> {
    List<PredictPrice> findByNameAndGrade(String name, Grade grade); // 이름, 등급으로 예측 가격 조회
}
