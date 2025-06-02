package com.project.Justick.Controller.Potato;

import com.project.Justick.DTO.Cabbage.CabbageRetailRequest;
import com.project.Justick.DTO.Onion.OnionRetailRequest;
import com.project.Justick.DTO.Potato.PotatoRetailRequest;
import com.project.Justick.Domain.Cabbage.CabbageRetail;
import com.project.Justick.Domain.Potato.PotatoRetail;
import com.project.Justick.Service.Cabbage.CabbageRetailService;
import com.project.Justick.Service.Potato.PotatoRetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/potato-retail")
public class PotatoRetailController {

    private final PotatoRetailService potatoRetailService;

    public PotatoRetailController(PotatoRetailService potatoRetailService) {
        this.potatoRetailService = potatoRetailService;
    }

    @GetMapping
    public List<PotatoRetail> getAllPotatoRetail() {
        return potatoRetailService.getAllPotatoRetail();
    }

    @PostMapping
    public ResponseEntity<String> createCabbageRetail(@RequestBody PotatoRetailRequest requests) {
        potatoRetailService.addPotatoRetail(requests);
        return ResponseEntity.ok("PotatoRetail created");
    }

    // batch 저장
    @PostMapping("/batch")
    public ResponseEntity<String> saveBatch(@RequestBody List<PotatoRetailRequest> requests) {
        potatoRetailService.saveAll(requests);
        return ResponseEntity.ok("Batch insert complete.");
    }

    // id로 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        potatoRetailService.deleteById(id);
        return ResponseEntity.ok("Deleted potato-retail with id: " + id);
    }

}
