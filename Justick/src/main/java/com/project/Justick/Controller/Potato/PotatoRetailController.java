package com.project.Justick.Controller.Potato;

import com.project.Justick.Controller.AgriculturalRetailController;
import com.project.Justick.DTO.Cabbage.CabbageRetailRequest;
import com.project.Justick.DTO.Onion.OnionRetailRequest;
import com.project.Justick.DTO.Potato.PotatoRetailRequest;
import com.project.Justick.Domain.Cabbage.CabbageRetail;
import com.project.Justick.Domain.Onion.OnionRetail;
import com.project.Justick.Domain.Potato.PotatoRetail;
import com.project.Justick.Service.Cabbage.CabbageRetailService;
import com.project.Justick.Service.Onion.OnionRetailService;
import com.project.Justick.Service.Potato.PotatoRetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/potato-retail")
public class PotatoRetailController extends AgriculturalRetailController<
        PotatoRetail,
        PotatoRetailRequest,
        PotatoRetailService> {

    public PotatoRetailController(PotatoRetailService service) {
        super(service, "PotatoRetail");
    }
}