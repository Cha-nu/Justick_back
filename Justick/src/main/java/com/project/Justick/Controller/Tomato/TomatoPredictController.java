package com.project.Justick.Controller.Tomato;

import com.project.Justick.Controller.AgriculturalPredictController;
import com.project.Justick.DTO.Tomato.TomatoPredictRequest;
import com.project.Justick.Domain.Tomato.TomatoPredict;
import com.project.Justick.Service.Tomato.TomatoPredictService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tomato-predict")
public class TomatoPredictController extends AgriculturalPredictController<TomatoPredict, TomatoPredictRequest> {

    public TomatoPredictController(TomatoPredictService service) {
        super(service, "Tomato");
    }
}
