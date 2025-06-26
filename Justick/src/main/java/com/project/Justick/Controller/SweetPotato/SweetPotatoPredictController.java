package com.project.Justick.Controller.SweetPotato;

import com.project.Justick.Controller.AgriculturalPredictController;
import com.project.Justick.DTO.SweetPotato.SweetPotatoPredictRequest;
import com.project.Justick.Domain.SweetPotato.SweetPotatoPredict;
import com.project.Justick.Service.SweetPotato.SweetPotatoPredictService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/sweetPotato-predict")
public class SweetPotatoPredictController extends AgriculturalPredictController<SweetPotatoPredict, SweetPotatoPredictRequest> {

    public SweetPotatoPredictController(SweetPotatoPredictService service) {
        super(service, "SweetPotato");
    }
}
