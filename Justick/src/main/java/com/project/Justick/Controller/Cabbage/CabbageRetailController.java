package com.project.Justick.Controller.Cabbage;

import com.project.Justick.DTO.Cabbage.CabbageRetailRequest;
import com.project.Justick.Domain.Cabbage.CabbageRetail;
import com.project.Justick.Domain.Potato.PotatoRetail;
import com.project.Justick.Service.Cabbage.CabbageRetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cabbage-retail")
public class CabbageRetailController {

    private final CabbageRetailService cabbageRetailService;

    public CabbageRetailController(CabbageRetailService cabbageRetailService) {
        this.cabbageRetailService = cabbageRetailService;
    }

    @GetMapping
    public List<CabbageRetail> getAllCabbageRetail() {
        return cabbageRetailService.getAllCabbageRetail();
    }

    @PostMapping
    public ResponseEntity<String> createCabbageRetail(@RequestBody CabbageRetailRequest requests) {
        cabbageRetailService.addCabbageRetail(requests);
        return ResponseEntity.ok("CabbageRetail created");
    }

    // batch 저장
    @PostMapping("/batch")
    public ResponseEntity<String> saveBatch(@RequestBody List<CabbageRetailRequest> requests) {
        cabbageRetailService.saveAll(requests);
        return ResponseEntity.ok("Batch insert complete.");
    }

    // id로 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        cabbageRetailService.deleteById(id);
        return ResponseEntity.ok("Deleted cabbage-retail with id: " + id);
    }

}
