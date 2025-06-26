package com.project.Justick.Controller.Radish;

import com.project.Justick.Controller.AgriculturalRetailController;
import com.project.Justick.DTO.Cabbage.CabbageRetailRequest;
import com.project.Justick.DTO.Potato.PotatoRetailRequest;
import com.project.Justick.DTO.Radish.RadishRetailRequest;
import com.project.Justick.Domain.Cabbage.CabbageRetail;
import com.project.Justick.Domain.Potato.PotatoRetail;
import com.project.Justick.Domain.Radish.RadishRetail;
import com.project.Justick.Service.Cabbage.CabbageRetailService;
import com.project.Justick.Service.Potato.PotatoRetailService;
import com.project.Justick.Service.Radish.RadishRetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/radish-retail")
public class RadishRetailController extends AgriculturalRetailController<
        RadishRetail,
        RadishRetailRequest,
        RadishRetailService> {

    public RadishRetailController(RadishRetailService service) {
        super(service, "RadishRetail");
    }
}
