package com.project.Justick.Repository.Tomato;

import com.project.Justick.Domain.Grade;
import com.project.Justick.Domain.Tomato.TomatoPredict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TomatoPredictRepository extends JpaRepository<TomatoPredict, Long> {

    List<TomatoPredict> findByGrade(Grade grade);

    @Query("SELECT COUNT(c) FROM TomatoPredict c WHERE c.grade = :grade")
    long countByGrade(@Param("grade") Grade grade);
}
