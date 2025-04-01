package com.project.Justick.Service;

import com.project.Justick.Domain.CabbagePrice;
import com.project.Justick.Domain.CabbagePriceRequest;
import com.project.Justick.Repository.CabbagePriceRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CabbagePriceService {
    private final CabbagePriceRepository repository;

    public CabbagePriceService(CabbagePriceRepository repository) {
        this.repository = repository;
    }

    public List<CabbagePrice> getAllPrices() {
        return repository.findAllByOrderByDateDesc();
    }

    public CabbagePrice save(CabbagePriceRequest request) {
        CabbagePrice entity = new CabbagePrice();
        entity.setDate(LocalDate.parse(request.getDate()));
        entity.setRating(request.getRating() == 1); // 1 → true (특), 0 → false (상)
        entity.setAveragePrice(request.getAveragePrice());

        return repository.save(entity);
    }
}
