package com.project.Justick.Repository.Tomato;

import com.project.Justick.Domain.Grade;
import com.project.Justick.Domain.Tomato.Tomato;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TomatoRepository extends JpaRepository<Tomato, Long> {
    List<Tomato> findByGrade(Grade grade);

    @Query("SELECT c FROM Tomato c WHERE c.grade = :grade ORDER BY c.year DESC, c.month DESC, c.day DESC")
    List<Tomato> findTopByGradeOrderByDateDesc(Grade grade, Pageable pageable);
    // Save a batch of Tomato entries
    @Modifying
    @Query("DELETE FROM Tomato c WHERE c.grade = :grade AND c.id = (SELECT c2.id FROM Cabbage c2 WHERE c2.grade = :grade ORDER BY c2.year, c2.month, c2.day LIMIT 1)")
    void deleteOldestByGrade(@Param("grade") Grade grade);

    // Save a single Tomato entry and delete the oldest one
    @Query("SELECT COUNT(c) FROM Tomato c WHERE c.grade = :grade")
    long countByGrade(@Param("grade") Grade grade);

    Optional<Tomato> findByGradeAndYearAndMonthAndDay(Grade grade, int year, int month, int day);

    List<Tomato> findByGradeOrderByYearAscMonthAscDayAsc(Grade grade);
}