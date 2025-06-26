package com.project.Justick.Controller.Radish;

import com.project.Justick.Controller.AgriculturalController;
import com.project.Justick.DTO.Cabbage.CabbageRequest;
import com.project.Justick.DTO.Radish.RadishRequest;
import com.project.Justick.Domain.Cabbage.Cabbage;
import com.project.Justick.Domain.Grade;
import com.project.Justick.Domain.Radish.Radish;
import com.project.Justick.Service.Cabbage.CabbageService;
import com.project.Justick.Service.Radish.RadishService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/radish")
public class RadishController extends AgriculturalController<Radish, RadishRequest> {
    public RadishController(RadishService service) {
        super(service, "Radish");
    }
}