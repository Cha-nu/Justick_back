package com.project.Justick.Controller.SweetPotato;


import com.project.Justick.DTO.SweetPotato.SweetPotatoPredictRequest;
import com.project.Justick.Domain.Grade;
import com.project.Justick.Domain.SweetPotato.SweetPotatoPredict;
import com.project.Justick.Service.SweetPotato.SweetPotatoPredictService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sweetPotato-predict")
public class SweetPotatoPredictController {

    private final SweetPotatoPredictService service;

    public SweetPotatoPredictController(SweetPotatoPredictService service) {
        this.service = service;
    }

    @GetMapping("/high-prices")
    public List<SweetPotatoPredict> getHighPredicts() {
        return service.findRecent20DaysWithForecast(Grade.HIGH);
    }

    @GetMapping("/special-prices")
    public List<SweetPotatoPredict> getSpecialPredicts() {
        return service.findRecent20DaysWithForecast(Grade.SPECIAL);
    }

    @PostMapping("/batch")
    public ResponseEntity<String> saveBatch(@RequestBody List<SweetPotatoPredictRequest> requests) {
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
