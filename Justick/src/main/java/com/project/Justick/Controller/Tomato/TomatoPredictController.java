package com.project.Justick.Controller.Tomato;

import com.project.Justick.DTO.Tomato.TomatoPredictRequest;
import com.project.Justick.Domain.Grade;
import com.project.Justick.Domain.Tomato.TomatoPredict;
import com.project.Justick.Service.Tomato.TomatoPredictService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tomato-predict")
public class TomatoPredictController {

    private final TomatoPredictService service;

    public TomatoPredictController(TomatoPredictService service) {
        this.service = service;
    }

    @GetMapping("/high-prices")
    public List<TomatoPredict> getHighPredicts() {
        return service.findRecentDaysWithForecast(Grade.HIGH);
    }

    @GetMapping("/special-prices")
    public List<TomatoPredict> getSpecialPredicts() {
        return service.findRecentDaysWithForecast(Grade.SPECIAL);
    }

    @PostMapping("/batch")
    public ResponseEntity<String> saveBatch(@RequestBody List<TomatoPredictRequest> requests) {
        service.saveAll(requests);
        return ResponseEntity.ok("Prediction batch insert complete.");
    }

    @PostMapping
    public ResponseEntity<String> saveOne(@RequestBody TomatoPredictRequest request) {
        service.saveOneAndDeleteOldest(request);
        return ResponseEntity.ok("Saved newest and deleted oldest if needed.");
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
