package com.project.Justick.Controller.SweetPotato;

import com.project.Justick.DTO.SweetPotato.SweetPotatoRequest;
import com.project.Justick.Domain.Grade;
import com.project.Justick.Domain.SweetPotato.SweetPotato;
import com.project.Justick.Service.SweetPotato.SweetPotatoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/sweetPotato")
public class SweetPotatoController {

    private final SweetPotatoService service;

    public SweetPotatoController(SweetPotatoService service) {
        this.service = service;
    }

    @GetMapping("/high-prices")
    public List<SweetPotato> getHighSweetPotatoPrices() {
        return service.findRecentDaysByGrade(Grade.HIGH);
    }

    @GetMapping("/special-prices")
    public List<SweetPotato> getSpecialSweetPotatoPrices() {
        return service.findRecentDaysByGrade(Grade.SPECIAL);
    }

    @PostMapping("/batch")
    public ResponseEntity<String> saveBatch(@RequestBody List<SweetPotatoRequest> requests) {
        service.saveAll(requests);
        return ResponseEntity.ok("Batch insert complete.");
    }

    @PostMapping
    public ResponseEntity<String> saveOne(@RequestBody SweetPotatoRequest request) {
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