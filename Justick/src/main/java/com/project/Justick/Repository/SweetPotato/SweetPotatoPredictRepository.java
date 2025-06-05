package com.project.Justick.Repository.SweetPotato;

import com.project.Justick.Domain.Cabbage.CabbagePredict;
import com.project.Justick.Domain.Grade;
import com.project.Justick.Domain.SweetPotato.SweetPotatoPredict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SweetPotatoPredictRepository extends JpaRepository<SweetPotatoPredict, Long> {

    List<SweetPotatoPredict> findByGrade(Grade grade);

    @Query("SELECT c FROM SweetPotatoPredict c WHERE c.grade = :grade ORDER BY c.year DESC, c.month DESC, c.day DESC LIMIT 1")
    SweetPotatoPredict findLatestByGrade(@Param("grade") Grade grade);

    @Query("SELECT c FROM SweetPotatoPredict c WHERE (c.year > :fromYear OR (c.year = :fromYear AND c.month > :fromMonth) OR (c.year = :fromYear AND c.month = :fromMonth AND c.day >= :fromDay)) " +
            "AND (c.year < :toYear OR (c.year = :toYear AND c.month < :toMonth) OR (c.year = :toYear AND c.month = :toMonth AND c.day <= :toDay)) " +
            "AND c.grade = :grade ORDER BY c.year, c.month, c.day")
    List<SweetPotatoPredict> findByDateRangeAndGrade(int fromYear, int fromMonth, int fromDay,
                                                 int toYear, int toMonth, int toDay,
                                                 Grade grade);


    @Query("SELECT COUNT(c) FROM SweetPotatoPredict c WHERE c.grade = :grade")
    long countByGrade(@Param("grade") Grade grade);
}
