package com.project.Justick.Controller.Cabbage;

import com.project.Justick.Controller.AgriculturalController;
import com.project.Justick.DTO.Cabbage.CabbageRequest;
import com.project.Justick.Domain.Cabbage.Cabbage;
import com.project.Justick.Service.Cabbage.CabbageService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cabbage")
public class CabbageController extends AgriculturalController<Cabbage, CabbageRequest> {
    public CabbageController(CabbageService service) {
        super(service, "Cabbage");
    }
}
