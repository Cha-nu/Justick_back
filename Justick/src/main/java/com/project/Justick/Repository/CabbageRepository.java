package com.project.Justick.Repository;

import com.project.Justick.Domain.Cabbage;
import com.project.Justick.Domain.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CabbageRepository extends JpaRepository<Cabbage, Long> {
    List<Cabbage> findByGrade(Grade grade);  // 등급별 조회도 가능
}
