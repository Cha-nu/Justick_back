package com.project.Justick.Repository.Potato;

import com.project.Justick.Domain.Cabbage.CabbagePredict;
import com.project.Justick.Domain.Grade;
import com.project.Justick.Domain.Potato.PotatoPredict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PotatoPredictRepository extends JpaRepository<PotatoPredict, Long> {

    List<PotatoPredict> findByGrade(Grade grade);

    @Query("SELECT COUNT(c) FROM PotatoPredict c WHERE c.grade = :grade")
    long countByGrade(@Param("grade") Grade grade);
}
