package com.project.Justick.Controller.Onion;

import com.project.Justick.Domain.Onion.OnionRetail;
import com.project.Justick.Service.Onion.OnionRetailService;
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
    public OnionRetail createOnionRetail(@RequestBody OnionRetail onionRetail) {
        return onionRetailService.addOnionRetail(onionRetail);
    }

}
