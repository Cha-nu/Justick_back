package com.project.Justick.Repository;

import com.project.Justick.Domain.AgriculturalPrice;
import com.project.Justick.Domain.Grade;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface AgriculturalRepository<T extends AgriculturalPrice> extends JpaRepository<T, Long> {
    List<T> findByGrade(Grade grade);

    @Query("SELECT e FROM #{#entityName} e WHERE e.grade = :grade ORDER BY e.year DESC, e.month DESC, e.day DESC")
    List<T> findTopByGradeOrderByDateDesc(@Param("grade") Grade grade, Pageable pageable);

    @Modifying
    @Query("DELETE FROM #{#entityName} e WHERE e.grade = :grade AND e.id = (" +
            "SELECT e2.id FROM #{#entityName} e2 WHERE e2.grade = :grade ORDER BY e2.year, e2.month, e2.day LIMIT 1)")
    void deleteOldestByGrade(@Param("grade") Grade grade);

    @Query("SELECT COUNT(e) FROM #{#entityName} e WHERE e.grade = :grade")
    long countByGrade(@Param("grade") Grade grade);

    Optional<T> findByGradeAndYearAndMonthAndDay(Grade grade, int year, int month, int day);

    List<T> findByGradeOrderByYearAscMonthAscDayAsc(Grade grade);
}