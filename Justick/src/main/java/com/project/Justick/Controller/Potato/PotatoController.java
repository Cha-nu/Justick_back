package com.project.Justick.Controller.Potato;

import com.project.Justick.DTO.Cabbage.CabbageRequest;
import com.project.Justick.DTO.Potato.PotatoRequest;
import com.project.Justick.Domain.Cabbage.Cabbage;
import com.project.Justick.Domain.Grade;
import com.project.Justick.Domain.Potato.Potato;
import com.project.Justick.Service.Cabbage.CabbageService;
import com.project.Justick.Service.Potato.PotatoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/potato")
public class PotatoController {

    private final PotatoService service;

    public PotatoController(PotatoService service) {
        this.service = service;
    }

    @GetMapping("/high-prices")
    public List<Potato> getHighPotatoPrices() {
        return service.findRecentDaysByGrade(Grade.HIGH);
    }

    @GetMapping("/special-prices")
    public List<Potato> getSpecialPotatoPrices() {
        return service.findRecentDaysByGrade(Grade.SPECIAL);
    }

    @PostMapping("/batch")
    public ResponseEntity<String> saveBatch(@RequestBody List<PotatoRequest> requests) {
        service.saveAll(requests);
        return ResponseEntity.ok("Batch insert complete.");
    }

    @PostMapping
    public ResponseEntity<String> saveOne(@RequestBody PotatoRequest request) {
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
        return ResponseEntity.ok("Potato data deleted with id: " + id);
    }
}