package com.project.Justick.Controller.Onion;

import com.project.Justick.Controller.AgriculturalRetailController;
import com.project.Justick.DTO.Onion.OnionRetailRequest;
import com.project.Justick.Domain.Onion.OnionRetail;
import com.project.Justick.Service.Onion.OnionRetailService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/onion-retail")
public class OnionRetailController extends AgriculturalRetailController<
        OnionRetail,
        OnionRetailRequest,
        OnionRetailService> {

    public OnionRetailController(OnionRetailService service) {
        super(service, "OnionRetail");
    }
}
