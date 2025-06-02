package com.project.Justick.Controller.Radish;

import com.project.Justick.DTO.Radish.RadishRequest;
import com.project.Justick.Domain.Grade;
import com.project.Justick.Domain.Radish.Radish;
import com.project.Justick.Service.Radish.RadishService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/radish")
public class RadishController {

    private final RadishService service;

    public RadishController(RadishService service) {
        this.service = service;
    }

    @GetMapping("/high-prices")
    public List<Radish> getHighRadishPrices() {
        return service.findRecentDaysByGrade(Grade.HIGH);
    }

    @GetMapping("/special-prices")
    public List<Radish> getSpecialRadishPrices() {
        return service.findRecentDaysByGrade(Grade.SPECIAL);
    }

    @PostMapping("/batch")
    public ResponseEntity<String> saveBatch(@RequestBody List<RadishRequest> requests) {
        service.saveAll(requests);
        return ResponseEntity.ok("Batch insert complete.");
    }

    @PostMapping
    public ResponseEntity<String> saveOne(@RequestBody RadishRequest request) {
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
        return ResponseEntity.ok("Radish data deleted with id: " + id);
    }
}