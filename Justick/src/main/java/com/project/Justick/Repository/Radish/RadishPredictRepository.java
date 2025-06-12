package com.project.Justick.Repository.Radish;

import com.project.Justick.Domain.Cabbage.CabbagePredict;
import com.project.Justick.Domain.Grade;
import com.project.Justick.Domain.Radish.RadishPredict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RadishPredictRepository extends JpaRepository<RadishPredict, Long> {

    List<RadishPredict> findByGrade(Grade grade);


    @Query("SELECT COUNT(c) FROM RadishPredict c WHERE c.grade = :grade")
    long countByGrade(@Param("grade") Grade grade);
}
