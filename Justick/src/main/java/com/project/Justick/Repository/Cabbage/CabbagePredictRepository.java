package com.project.Justick.Repository.Cabbage;

import com.project.Justick.Domain.Cabbage.CabbagePredict;
import com.project.Justick.Domain.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CabbagePredictRepository extends JpaRepository<CabbagePredict, Long> {

    List<CabbagePredict> findByGrade(Grade grade);

    @Query("SELECT c FROM CabbagePredict c WHERE c.grade = :grade ORDER BY c.year DESC, c.month DESC, c.day DESC LIMIT 1")
    CabbagePredict findLatestByGrade(@Param("grade") Grade grade);

    @Query("SELECT c FROM CabbagePredict c WHERE (c.year > :fromYear OR (c.year = :fromYear AND c.month > :fromMonth) OR (c.year = :fromYear AND c.month = :fromMonth AND c.day >= :fromDay)) " +
            "AND (c.year < :toYear OR (c.year = :toYear AND c.month < :toMonth) OR (c.year = :toYear AND c.month = :toMonth AND c.day <= :toDay)) " +
            "AND c.grade = :grade ORDER BY c.year, c.month, c.day")
    List<CabbagePredict> findByDateRangeAndGrade(int fromYear, int fromMonth, int fromDay,
                                                 int toYear, int toMonth, int toDay,
                                                 Grade grade);


    @Query("SELECT COUNT(c) FROM CabbagePredict c WHERE c.grade = :grade")
    long countByGrade(@Param("grade") String grade);
}
