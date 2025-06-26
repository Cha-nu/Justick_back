package com.project.Justick.Controller;

import com.project.Justick.Domain.AgriculturalPrice;
import com.project.Justick.Domain.Grade;
import com.project.Justick.Service.AgriculturalPredictService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

public abstract class AgriculturalPredictController<T extends AgriculturalPrice, R> {

    protected final AgriculturalPredictService<T, R> service;
    protected final String name;

    protected AgriculturalPredictController(AgriculturalPredictService<T, R> service, String name) {
        this.service = service;
        this.name = name;
    }

    @GetMapping("/high-prices")
    public List<T> getHighPrices() {
        return service.findRecentDaysWithForecast(Grade.HIGH);
    }

    @GetMapping("/special-prices")
    public List<T> getSpecialPrices() {
        return service.findRecentDaysWithForecast(Grade.SPECIAL);
    }

    @PostMapping("/batch")
    public ResponseEntity<String> saveBatch(@RequestBody List<R> requests) {
        service.saveAll(requests);
        return ResponseEntity.ok("Prediction batch insert complete.");
    }

    @PostMapping
    public ResponseEntity<String> saveOne(@RequestBody R request) {
        service.saveOneAndDeleteOldest(request);
        return ResponseEntity.ok("Saved newest and deleted oldest if needed.");
    }

    @GetMapping("/high-weekly")
    public Map<String, Map<String, Integer>> getHighWeeklyAvg() {
        return service.getWeeklyAverages(Grade.HIGH);
    }

    @GetMapping("/special-weekly")
    public Map<String, Map<String, Integer>> getSpecialWeeklyAvg() {
        return service.getWeeklyAverages(Grade.SPECIAL);
    }

    @GetMapping("/high-monthly")
    public Map<String, Map<String, Integer>> getHighMonthlyAvg() {
        return service.getMonthlyAverages(Grade.HIGH);
    }

    @GetMapping("/special-monthly")
    public Map<String, Map<String, Integer>> getSpecialMonthlyAvg() {
        return service.getMonthlyAverages(Grade.SPECIAL);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok(name + " prediction deleted.");
    }
}
