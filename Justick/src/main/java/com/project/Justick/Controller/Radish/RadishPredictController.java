package com.project.Justick.Controller.Radish;


import com.project.Justick.Controller.AgriculturalPredictController;
import com.project.Justick.DTO.Radish.RadishPredictRequest;
import com.project.Justick.Domain.Radish.RadishPredict;
import com.project.Justick.Service.Radish.RadishPredictService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/radish-predict")
public class RadishPredictController extends AgriculturalPredictController<RadishPredict, RadishPredictRequest> {

    public RadishPredictController(RadishPredictService service) {
        super(service, "Onion");
    }
}