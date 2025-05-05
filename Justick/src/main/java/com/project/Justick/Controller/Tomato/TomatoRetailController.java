package com.project.Justick.Controller.Tomato;

import com.project.Justick.Domain.Cabbage.CabbageRetail;
import com.project.Justick.Domain.Tomato.TomatoRetail;
import com.project.Justick.Service.Cabbage.CabbageRetailService;
import com.project.Justick.Service.Tomato.TomatoRetailService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tomato-retail")
public class TomatoRetailController {

    private final TomatoRetailService tomatoRetailService;

    public TomatoRetailController(TomatoRetailService tomatoRetailService) {
        this.tomatoRetailService = tomatoRetailService;
    }

    @GetMapping
    public List<TomatoRetail> getAllTomatoRetail() {
        return tomatoRetailService.getAllTomatoRetail();
    }

    @PostMapping
    public TomatoRetail createTomatoRetail(@RequestBody TomatoRetail tomatoRetail) {
        return tomatoRetailService.addTomatoRetail(tomatoRetail);
    }

}
