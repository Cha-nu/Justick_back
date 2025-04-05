package com.project.Justick.Controller;

import com.project.Justick.DTO.CabbageRequest;
import com.project.Justick.Domain.Cabbage;
import com.project.Justick.Domain.PredictPrice;
import com.project.Justick.Service.CabbageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cabbage")
public class CabbageController {

    private final CabbageService service;

    public CabbageController(CabbageService service) {
        this.service = service;
    }

    // 상 배추 가격 정보 조회
    @GetMapping("/HighPrices")
    public List<Cabbage> getHighCabbagePrices() {
        return service.getHighPrices();
    }

    // 특 배추 가격 정보 조회
    @GetMapping("/SpePrices")
    public List<Cabbage> getSpeCabbagePrices() {
        return service.getSpePrices();
    }

    // 상 배추 예측 가격 정보 조회
    @GetMapping("/HighPredict")
    public List<PredictPrice> getHighPredictPrice() {
        return service.getHighPredictPrice();
    }

    // 특 배추 예측 가격 정보 조회
    @GetMapping("/SpePredict")
    public List<PredictPrice> getSpePredictPrice() {
        return service.getSpePredictPrice();
    }

    // 배추 정보 저장
    @PostMapping
    public ResponseEntity<Cabbage> save(@RequestBody CabbageRequest request) {
        return ResponseEntity.ok(service.save(request));
    }
}
