package com.project.Justick.Controller;

import com.project.Justick.Domain.AgriculturalPrice;
import com.project.Justick.Domain.Grade;
import com.project.Justick.Service.AgriculturalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

public abstract class AgriculturalController<T extends AgriculturalPrice, R> {

    protected final AgriculturalService<T, R> service;
    protected final String name;

    protected AgriculturalController(AgriculturalService<T, R> service, String name) {
        this.service = service;
        this.name = name;
    }

    @GetMapping("/high-prices")
    public List<T> getHighPrices() {
        return service.findRecentDaysByGrade(Grade.HIGH);
    }

    @GetMapping("/special-prices")
    public List<T> getSpecialPrices() {
        return service.findRecentDaysByGrade(Grade.SPECIAL);
    }

    @PostMapping("/batch")
    public ResponseEntity<String> saveBatch(@RequestBody List<R> requests) {
        service.saveAll(requests);
        return ResponseEntity.ok("Batch insert complete.");
    }

    @PostMapping
    public ResponseEntity<String> saveOne(@RequestBody R request) {
        service.saveOneAndDeleteOldest(request);
        return ResponseEntity.ok("Saved newest and deleted oldest.");
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
        return ResponseEntity.ok(name + " data deleted with id: " + id);
    }
}
