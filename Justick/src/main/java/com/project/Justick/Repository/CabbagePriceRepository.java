package com.project.Justick.Repository;

import com.project.Justick.Domain.CabbagePrice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CabbagePriceRepository extends JpaRepository<CabbagePrice, Long> {
    List<CabbagePrice> findAllByOrderByDateDesc();
}
