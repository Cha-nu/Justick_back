package com.project.Justick.Repository.SweetPotato;

import com.project.Justick.Domain.Grade;
import com.project.Justick.Domain.SweetPotato.SweetPotatoPredict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SweetPotatoPredictRepository extends JpaRepository<SweetPotatoPredict, Long> {

    List<SweetPotatoPredict> findByGrade(Grade grade);

    @Query("SELECT COUNT(c) FROM SweetPotatoPredict c WHERE c.grade = :grade")
    long countByGrade(@Param("grade") Grade grade);
}
