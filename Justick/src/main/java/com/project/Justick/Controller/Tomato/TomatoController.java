package com.project.Justick.Controller.Tomato;


import com.project.Justick.DTO.Tomato.TomatoRequest;
import com.project.Justick.Domain.Grade;
import com.project.Justick.Domain.Tomato.Tomato;
import com.project.Justick.Service.Tomato.TomatoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tomato")
public class TomatoController {

    private final TomatoService service;

    public TomatoController(TomatoService service) {
        this.service = service;
    }

    @GetMapping("/high-prices")
    public List<Tomato> getHighTomatoPrices() {
        return service.findRecentDaysByGrade(Grade.HIGH);
    }

    @GetMapping("/special-prices")
    public List<Tomato> getSpecialTomatoPrices() {
        return service.findRecentDaysByGrade(Grade.SPECIAL);
    }

    @PostMapping("/batch")
    public ResponseEntity<String> saveBatch(@RequestBody List<TomatoRequest> requests) {
        service.saveAll(requests);
        return ResponseEntity.ok("Batch insert complete.");
    }

    @PostMapping
    public ResponseEntity<String> saveOne(@RequestBody TomatoRequest request) {
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
}