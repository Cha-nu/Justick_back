package com.project.Justick.Controller;


import com.project.Justick.DTO.CabbageRequest;
import com.project.Justick.DTO.PredictRequest;
import com.project.Justick.Domain.Cabbage;
import com.project.Justick.Domain.PredictPrice;
import com.project.Justick.Service.PredictService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/predict")
public class PredictController {

    private final PredictService service;

    public PredictController(PredictService service) {
        this.service = service;
    }
    @PostMapping
    public ResponseEntity<PredictPrice> save(@RequestBody PredictRequest request) {
        return ResponseEntity.ok(service.save(request));
    }

}
