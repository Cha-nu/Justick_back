package com.project.Justick.Controller.Radish;

import com.project.Justick.DTO.Cabbage.CabbageRetailRequest;
import com.project.Justick.DTO.Radish.RadishRetailRequest;
import com.project.Justick.Domain.Cabbage.CabbageRetail;
import com.project.Justick.Domain.Radish.RadishRetail;
import com.project.Justick.Service.Cabbage.CabbageRetailService;
import com.project.Justick.Service.Radish.RadishRetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/radish-retail")
public class RadishRetailController {

    private final RadishRetailService radishRetailService;

    public RadishRetailController(RadishRetailService radishRetailService) {
        this.radishRetailService = radishRetailService;
    }

    @GetMapping
    public List<RadishRetail> getAllRadishRetail() {
        return radishRetailService.getAllRadishRetail();
    }

    @PostMapping
    public ResponseEntity<String> createRadishRetail(@RequestBody RadishRetailRequest requests) {
        radishRetailService.addRadishRetail(requests);
        return ResponseEntity.ok("RadishRetail created");
    }

    // batch 저장
    @PostMapping("/batch")
    public ResponseEntity<String> saveBatch(@RequestBody List<RadishRetailRequest> requests) {
        radishRetailService.saveAll(requests);
        return ResponseEntity.ok("Batch insert complete.");
    }

    // id로 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        radishRetailService.deleteById(id);
        return ResponseEntity.ok("Deleted Radish-retail with id: " + id);
    }

}
