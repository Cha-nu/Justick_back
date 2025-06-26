package com.project.Justick.Controller.Onion;

import com.project.Justick.Controller.AgriculturalController;
import com.project.Justick.DTO.Cabbage.CabbageRequest;
import com.project.Justick.DTO.Onion.OnionRequest;
import com.project.Justick.Domain.Cabbage.Cabbage;
import com.project.Justick.Domain.Grade;
import com.project.Justick.Domain.Onion.Onion;
import com.project.Justick.Service.Cabbage.CabbageService;
import com.project.Justick.Service.Onion.OnionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/onion")
public class OnionController extends AgriculturalController<Onion, OnionRequest> {
    public OnionController(OnionService service) {
        super(service, "Onion");
    }
}