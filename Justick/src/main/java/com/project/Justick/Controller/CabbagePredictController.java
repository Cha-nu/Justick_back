package com.project.Justick.Controller;

import com.project.Justick.Domain.CabbagePredict;
import com.project.Justick.Domain.Grade;
import com.project.Justick.DTO.CabbageRequest;
import com.project.Justick.Service.CabbagePredictService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cabbage-predict")
public class CabbagePredictController {

    private final CabbagePredictService service;

    public CabbagePredictController(CabbagePredictService service) {
        this.service = service;
    }

    @GetMapping("/high-prices")
    public List<CabbagePredict> getHighPredicts() {
        return service.findByGrade(Grade.HIGH);
    }

    @GetMapping("/special-prices")
    public List<CabbagePredict> getSpecialPredicts() {
        return service.findByGrade(Grade.SPECIAL);
    }

    @PostMapping("/batch")
    public ResponseEntity<String> saveBatch(@RequestBody List<CabbageRequest> requests) {
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
