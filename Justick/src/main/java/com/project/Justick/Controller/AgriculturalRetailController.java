package com.project.Justick.Controller;

import com.project.Justick.Domain.AgriculturalRetail;
import com.project.Justick.Service.AgriculturalRetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public abstract class AgriculturalRetailController<
        T extends AgriculturalRetail,
        R,
        S extends AgriculturalRetailService<T, R>> {

    protected final S service;
    protected final String name;

    protected AgriculturalRetailController(S service, String name) {
        this.service = service;
        this.name = name;
    }

    @GetMapping
    public List<T> getAll() {
        return service.findAll();
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody R request) {
        service.saveWithLimit(request, 28);
        return ResponseEntity.ok(name + " created");
    }

    @PostMapping("/batch")
    public ResponseEntity<String> saveBatch(@RequestBody List<R> requests) {
        service.saveAll(requests);
        return ResponseEntity.ok(name + " batch insert complete.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok(name + " deleted with id: " + id);
    }
}
