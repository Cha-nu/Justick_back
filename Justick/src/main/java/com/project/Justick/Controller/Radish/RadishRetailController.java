package com.project.Justick.Controller.Radish;

import com.project.Justick.Domain.Cabbage.CabbageRetail;
import com.project.Justick.Domain.Radish.RadishRetail;
import com.project.Justick.Service.Cabbage.CabbageRetailService;
import com.project.Justick.Service.Radish.RadishRetailService;
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
    public RadishRetail createRadishRetail(@RequestBody RadishRetail radishRetail) {
        return radishRetailService.addRadishRetail(radishRetail);
    }

}
