package com.project.Justick.Controller.Cabbage;

import com.project.Justick.Controller.AgriculturalPredictController;
import com.project.Justick.DTO.Cabbage.CabbagePredictRequest;
import com.project.Justick.Domain.Cabbage.CabbagePredict;
import com.project.Justick.Service.Cabbage.CabbagePredictService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/cabbage-predict")
public class CabbagePredictController extends AgriculturalPredictController<CabbagePredict, CabbagePredictRequest> {

    public CabbagePredictController(CabbagePredictService service) {
        super(service, "Cabbage");
    }
}
