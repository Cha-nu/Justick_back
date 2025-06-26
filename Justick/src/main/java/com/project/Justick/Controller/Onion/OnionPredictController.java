package com.project.Justick.Controller.Onion;

import com.project.Justick.Controller.AgriculturalPredictController;
import com.project.Justick.DTO.Onion.OnionPredictRequest;
import com.project.Justick.Domain.Onion.OnionPredict;
import com.project.Justick.Service.Onion.OnionPredictService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/onion-predict")
public class OnionPredictController extends AgriculturalPredictController<OnionPredict, OnionPredictRequest> {

    public OnionPredictController(OnionPredictService service) {
        super(service, "Onion");
    }
}
