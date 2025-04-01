package com.project.Justick.Controller;

import com.project.Justick.Domain.CabbagePrice;
import com.project.Justick.Domain.CabbagePriceRequest;
import com.project.Justick.Service.CabbagePriceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cabbage")
public class CabbagePriceController {

    private final CabbagePriceService service;

    public CabbagePriceController(CabbagePriceService service) {
        this.service = service;
    }

    @GetMapping("/prices")
    public List<CabbagePrice> getCabbagePrices() {
        return service.getAllPrices();
    }

    @PostMapping
    public ResponseEntity<CabbagePrice> save(@RequestBody CabbagePriceRequest request) {
        return ResponseEntity.ok(service.save(request));
    }
    // 가격 예측 값 get
//    @GetMapping("/predict")
//    public  getPredictCabbagePrice() {
//
//    }
}
