package com.project.Justick.Controller.SweetPotato;

import com.project.Justick.Domain.Cabbage.CabbageRetail;
import com.project.Justick.Domain.SweetPotato.SweetPotatoRetail;
import com.project.Justick.Service.Cabbage.CabbageRetailService;
import com.project.Justick.Service.SweetPotato.SweetPotatoRetailService;
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
    public SweetPotatoRetail createSweetPotatoRetail(@RequestBody SweetPotatoRetail sweetPotatoRetail) {
        return sweetPotatoRetailService.addSweetPotatoRetail(sweetPotatoRetail);
    }

}
