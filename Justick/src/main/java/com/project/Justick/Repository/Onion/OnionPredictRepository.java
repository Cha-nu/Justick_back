package com.project.Justick.Repository.Onion;

import com.project.Justick.Domain.Grade;
import com.project.Justick.Domain.Onion.OnionPredict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OnionPredictRepository extends JpaRepository<OnionPredict, Long> {

    List<OnionPredict> findByGrade(Grade grade);

    @Query("SELECT COUNT(c) FROM OnionPredict c WHERE c.grade = :grade")
    long countByGrade(@Param("grade") Grade grade);
}
