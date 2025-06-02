package com.project.Justick.Controller.SweetPotato;

import com.project.Justick.DTO.Cabbage.CabbageRetailRequest;
import com.project.Justick.DTO.SweetPotato.SweetPotatoRetailRequest;
import com.project.Justick.Domain.Cabbage.CabbageRetail;
import com.project.Justick.Domain.SweetPotato.SweetPotatoRetail;
import com.project.Justick.Service.Cabbage.CabbageRetailService;
import com.project.Justick.Service.SweetPotato.SweetPotatoRetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sweetPotato-retail")
public class SweetPotatoRetailController {

    private final SweetPotatoRetailService sweetPotatoRetailService;

    public SweetPotatoRetailController(SweetPotatoRetailService sweetPotatoRetailService) {
        this.sweetPotatoRetailService = sweetPotatoRetailService;
    }

    @GetMapping
    public List<SweetPotatoRetail> getAllSweetPotatoRetail() {
        return sweetPotatoRetailService.getAllSweetPotatoRetail();
    }

    @PostMapping
    public ResponseEntity<String> createSweetPotatoRetail(@RequestBody SweetPotatoRetailRequest requests) {
        sweetPotatoRetailService.addSweetPotatoRetail(requests);
        return ResponseEntity.ok("SweetPotatoRetail created");
    }

    // batch 저장
    @PostMapping("/batch")
    public ResponseEntity<String> saveBatch(@RequestBody List<SweetPotatoRetailRequest> requests) {
        sweetPotatoRetailService.saveAll(requests);
        return ResponseEntity.ok("Batch insert complete.");
    }

    // id로 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        sweetPotatoRetailService.deleteById(id);
        return ResponseEntity.ok("Deleted SweetPotato-retail with id: " + id);
    }

}
