package com.project.Justick.Repository;

import com.project.Justick.Domain.Cabbage;
import com.project.Justick.Domain.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CabbageRepository extends JpaRepository<Cabbage, Long> {
    List<Cabbage> findByGrade(Grade grade);

    @Query("SELECT c FROM Cabbage c WHERE c.grade = :grade ORDER BY c.year DESC, c.month DESC, c.day DESC LIMIT 1")
    Cabbage findLatestByGrade(@Param("grade") Grade grade);

    @Query("SELECT c FROM Cabbage c WHERE (c.year > :fromYear OR (c.year = :fromYear AND c.month > :fromMonth) OR (c.year = :fromYear AND c.month = :fromMonth AND c.day >= :fromDay)) " +
            "AND (c.year < :toYear OR (c.year = :toYear AND c.month < :toMonth) OR (c.year = :toYear AND c.month = :toMonth AND c.day <= :toDay)) " +
            "AND c.grade = :grade ORDER BY c.year, c.month, c.day")
    List<Cabbage> findByDateRangeAndGrade(int fromYear, int fromMonth, int fromDay,
                                          int toYear, int toMonth, int toDay,
                                          Grade grade);

    @Modifying
    @Query("DELETE FROM Cabbage c WHERE c.grade = :grade AND c.id = (SELECT c2.id FROM Cabbage c2 WHERE c2.grade = :grade ORDER BY c2.year, c2.month, c2.day LIMIT 1)")
    void deleteOldestByGrade(@Param("grade") Grade grade);

    @Query("SELECT COUNT(c) FROM Cabbage c WHERE c.grade = :grade")
    long countByGrade(@Param("grade") String grade);
}