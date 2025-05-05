package com.project.Justick.Controller.Potato;

import com.project.Justick.Domain.Cabbage.CabbageRetail;
import com.project.Justick.Domain.Potato.PotatoRetail;
import com.project.Justick.Service.Cabbage.CabbageRetailService;
import com.project.Justick.Service.Potato.PotatoRetailService;
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
    public PotatoRetail createCabbageRetail(@RequestBody PotatoRetail potatoRetail) {
        return potatoRetailService.addPotatoRetail(potatoRetail);
    }

}
