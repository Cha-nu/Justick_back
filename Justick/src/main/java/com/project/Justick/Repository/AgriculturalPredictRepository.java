package com.project.Justick.Repository;

import com.project.Justick.Domain.AgriculturalPrice;
import com.project.Justick.Domain.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;

@NoRepositoryBean
public interface AgriculturalPredictRepository<T extends AgriculturalPrice> extends JpaRepository<T, Long> {

    List<T> findByGrade(Grade grade);

    @Query("SELECT COUNT(e) FROM #{#entityName} e WHERE e.grade = :grade")
    long countByGrade(@Param("grade") Grade grade);
}