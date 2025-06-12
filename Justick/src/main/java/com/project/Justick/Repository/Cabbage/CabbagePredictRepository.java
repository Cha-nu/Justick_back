package com.project.Justick.Repository.Cabbage;

import com.project.Justick.Domain.Cabbage.CabbagePredict;
import com.project.Justick.Domain.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CabbagePredictRepository extends JpaRepository<CabbagePredict, Long> {

    List<CabbagePredict> findByGrade(Grade grade);

    @Query("SELECT COUNT(c) FROM CabbagePredict c WHERE c.grade = :grade")
    long countByGrade(@Param("grade") Grade grade);

}
