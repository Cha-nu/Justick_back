package com.project.Justick.Controller.Tomato;

import com.project.Justick.DTO.Tomato.TomatoRetailRequest;
import com.project.Justick.Domain.Tomato.TomatoRetail;
import com.project.Justick.Service.Tomato.TomatoRetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tomato-retail")
public class TomatoRetailController {

    private final TomatoRetailService tomatoRetailService;

    public TomatoRetailController(TomatoRetailService tomatoRetailService) {
        this.tomatoRetailService = tomatoRetailService;
    }

    @GetMapping
    public List<TomatoRetail> getAllTomatoRetail() {
        return tomatoRetailService.getAllTomatoRetail();
    }

    @PostMapping
    public ResponseEntity<String> createTomatoRetail(@RequestBody TomatoRetailRequest requests) {
        tomatoRetailService.addTomatoRetail(requests);
        return ResponseEntity.ok("TomatoRetail created");
    }

    // batch 저장
    @PostMapping("/batch")
    public ResponseEntity<String> saveBatch(@RequestBody List<TomatoRetailRequest> requests) {
        tomatoRetailService.saveAll(requests);
        return ResponseEntity.ok("Batch insert complete.");
    }

    // id로 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        tomatoRetailService.deleteById(id);
        return ResponseEntity.ok("Deleted Tomato-retail with id: " + id);
    }

}
