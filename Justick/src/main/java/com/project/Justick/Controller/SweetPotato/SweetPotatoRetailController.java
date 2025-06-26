package com.project.Justick.Controller.SweetPotato;

import com.project.Justick.Controller.AgriculturalRetailController;
import com.project.Justick.DTO.SweetPotato.SweetPotatoRetailRequest;
import com.project.Justick.Domain.SweetPotato.SweetPotatoRetail;
import com.project.Justick.Service.SweetPotato.SweetPotatoRetailService;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/sweetPotato-retail")
public class SweetPotatoRetailController extends AgriculturalRetailController<
        SweetPotatoRetail,
        SweetPotatoRetailRequest,
        SweetPotatoRetailService> {

    public SweetPotatoRetailController(SweetPotatoRetailService service) {
        super(service, "SweetPotatoRetail");
    }
}
