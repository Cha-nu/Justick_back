package com.project.Justick.Repository.SweetPotato;

import com.project.Justick.Domain.Cabbage.Cabbage;
import com.project.Justick.Domain.Grade;
import com.project.Justick.Domain.SweetPotato.SweetPotato;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SweetPotatoRepository extends JpaRepository<SweetPotato, Long> {
    List<SweetPotato> findByGrade(Grade grade);

    @Query("SELECT c FROM SweetPotato c WHERE c.grade = :grade ORDER BY c.year DESC, c.month DESC, c.day DESC")
    List<SweetPotato> findTopByGradeOrderByDateDesc(Grade grade, Pageable pageable);
    // Save a batch of Cabbage entries
    @Modifying
    @Query("DELETE FROM SweetPotato c WHERE c.grade = :grade AND c.id = (SELECT c2.id FROM Cabbage c2 WHERE c2.grade = :grade ORDER BY c2.year, c2.month, c2.day LIMIT 1)")
    void deleteOldestByGrade(@Param("grade") Grade grade);

    // Save a single Cabbage entry and delete the oldest one
    @Query("SELECT COUNT(c) FROM SweetPotato c WHERE c.grade = :grade")
    long countByGrade(@Param("grade") String grade);
}