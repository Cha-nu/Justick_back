package com.project.Justick.Controller.Potato;

import com.project.Justick.Controller.AgriculturalController;
import com.project.Justick.DTO.Cabbage.CabbageRequest;
import com.project.Justick.DTO.Potato.PotatoRequest;
import com.project.Justick.Domain.Cabbage.Cabbage;
import com.project.Justick.Domain.Grade;
import com.project.Justick.Domain.Potato.Potato;
import com.project.Justick.Service.Cabbage.CabbageService;
import com.project.Justick.Service.Potato.PotatoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/potato")
public class PotatoController extends AgriculturalController<Potato, PotatoRequest> {
    public PotatoController(PotatoService service) {
        super(service, "Potato");
    }
}