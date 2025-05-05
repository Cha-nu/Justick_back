package com.project.Justick.Controller.Onion;


import com.project.Justick.DTO.Onion.OnionRequest;
import com.project.Justick.Domain.Grade;
import com.project.Justick.Domain.Onion.OnionPredict;
import com.project.Justick.Service.Onion.OnionPredictService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/onion-predict")
public class OnionPredictController {

    private final OnionPredictService service;

    public OnionPredictController(OnionPredictService service) {
        this.service = service;
    }

    @GetMapping("/high-prices")
    public List<OnionPredict> getHighPredicts() {
        return service.findRecent20DaysWithForecast(Grade.HIGH);
    }

    @GetMapping("/special-prices")
    public List<OnionPredict> getSpecialPredicts() {
        return service.findRecent20DaysWithForecast(Grade.SPECIAL);
    }

    @PostMapping("/batch")
    public ResponseEntity<String> saveBatch(@RequestBody List<OnionRequest> requests) {
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
