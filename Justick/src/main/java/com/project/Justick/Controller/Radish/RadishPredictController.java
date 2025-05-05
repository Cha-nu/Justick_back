package com.project.Justick.Controller.Radish;


import com.project.Justick.DTO.Radish.RadishRequest;
import com.project.Justick.Domain.Grade;
import com.project.Justick.Domain.Radish.RadishPredict;
import com.project.Justick.Service.Radish.RadishPredictService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/radish-predict")
public class RadishPredictController {

    private final RadishPredictService service;

    public RadishPredictController(RadishPredictService service) {
        this.service = service;
    }

    @GetMapping("/high-prices")
    public List<RadishPredict> getHighPredicts() {
        return service.findRecent20DaysWithForecast(Grade.HIGH);
    }

    @GetMapping("/special-prices")
    public List<RadishPredict> getSpecialPredicts() {
        return service.findRecent20DaysWithForecast(Grade.SPECIAL);
    }

    @PostMapping("/batch")
    public ResponseEntity<String> saveBatch(@RequestBody List<RadishRequest> requests) {
        service.saveAll(requests);
        return ResponseEntity.ok("Prediction batch insert complete.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok("Prediction deleted with id: " + id);
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
}
