package com.project.Justick.Repository.Radish;

import com.project.Justick.Domain.Grade;
import com.project.Justick.Domain.Radish.Radish;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RadishRepository extends JpaRepository<Radish, Long> {
    List<Radish> findByGrade(Grade grade);

    @Query("SELECT c FROM Radish c WHERE c.grade = :grade ORDER BY c.year DESC, c.month DESC, c.day DESC")
    List<Radish> findTopByGradeOrderByDateDesc(Grade grade, Pageable pageable);
    // Save a batch of Radish entries
    @Modifying
    @Query("DELETE FROM Radish c WHERE c.grade = :grade AND c.id = (SELECT c2.id FROM Cabbage c2 WHERE c2.grade = :grade ORDER BY c2.year, c2.month, c2.day LIMIT 1)")
    void deleteOldestByGrade(@Param("grade") Grade grade);

    // Save a single Radish entry and delete the oldest one
    @Query("SELECT COUNT(c) FROM Radish c WHERE c.grade = :grade")
    long countByGrade(@Param("grade") Grade grade);

    Optional<Radish> findByGradeAndYearAndMonthAndDay(Grade grade, int year, int month, int day);

    List<Radish> findByGradeOrderByYearAscMonthAscDayAsc(Grade grade);
}