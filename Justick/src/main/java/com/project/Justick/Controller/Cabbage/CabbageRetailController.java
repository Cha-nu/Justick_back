package com.project.Justick.Controller.Cabbage;

import com.project.Justick.Domain.Cabbage.CabbageRetail;
import com.project.Justick.Service.Cabbage.CabbageRetailService;
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
    public CabbageRetail createCabbageRetail(@RequestBody CabbageRetail cabbageRetail) {
        return cabbageRetailService.addCabbageRetail(cabbageRetail);
    }

}
