package com.project.Justick.Repository.Onion;

import com.project.Justick.Domain.Cabbage.Cabbage;
import com.project.Justick.Domain.Grade;
import com.project.Justick.Domain.Onion.Onion;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OnionRepository extends JpaRepository<Onion, Long> {
    List<Onion> findByGrade(Grade grade);

    @Query("SELECT c FROM Onion c WHERE c.grade = :grade ORDER BY c.year DESC, c.month DESC, c.day DESC")
    List<Onion> findTopByGradeOrderByDateDesc(Grade grade, Pageable pageable);

    @Modifying
    @Query("DELETE FROM Onion c WHERE c.grade = :grade AND c.id = (SELECT c2.id FROM Onion c2 WHERE c2.grade = :grade ORDER BY c2.year, c2.month, c2.day LIMIT 1)")
    void deleteOldestByGrade(@Param("grade") Grade grade);

    @Query("SELECT COUNT(c) FROM Onion c WHERE c.grade = :grade")
    long countByGrade(@Param("grade") String grade);

    Optional<Onion> findByGradeAndYearAndMonthAndDay(Grade grade, int year, int month, int day);
}