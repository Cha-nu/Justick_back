package com.project.Justick.Controller.Onion;

import com.project.Justick.DTO.Cabbage.CabbageRetailRequest;
import com.project.Justick.DTO.Onion.OnionRetailRequest;
import com.project.Justick.Domain.Onion.OnionRetail;
import com.project.Justick.Service.Onion.OnionRetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/onion-retail")
public class OnionRetailController {

    private final OnionRetailService onionRetailService;

    public OnionRetailController(OnionRetailService onionRetailService) {
        this.onionRetailService = onionRetailService;
    }

    @GetMapping
    public List<OnionRetail> getAllOnionRetail() {
        return onionRetailService.getAllOnionRetail();
    }

    @PostMapping
    public ResponseEntity<String> createOnionRetail(@RequestBody OnionRetailRequest requests) {
        onionRetailService.addOnionRetail(requests);
        return ResponseEntity.ok("OnionRetail created");
    }

    // batch 저장
    @PostMapping("/batch")
    public ResponseEntity<String> saveBatch(@RequestBody List<OnionRetailRequest> requests) {
        onionRetailService.saveAll(requests);
        return ResponseEntity.ok("Batch insert complete.");
    }

    // id로 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        onionRetailService.deleteById(id);
        return ResponseEntity.ok("Deleted cabbage-retail with id: " + id);
    }

}
