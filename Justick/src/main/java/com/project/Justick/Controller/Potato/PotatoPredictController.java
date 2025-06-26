package com.project.Justick.Controller.Potato;

import com.project.Justick.Controller.AgriculturalPredictController;
import com.project.Justick.DTO.Potato.PotatoPredictRequest;
import com.project.Justick.Domain.Potato.PotatoPredict;
import com.project.Justick.Service.Potato.PotatoPredictService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/potato-predict")
public class PotatoPredictController extends AgriculturalPredictController<PotatoPredict, PotatoPredictRequest> {

    public PotatoPredictController(PotatoPredictService service) {
        super(service, "Potato");
    }
}