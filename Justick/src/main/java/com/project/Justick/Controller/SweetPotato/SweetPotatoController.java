package com.project.Justick.Controller.SweetPotato;

import com.project.Justick.Controller.AgriculturalController;
import com.project.Justick.DTO.SweetPotato.SweetPotatoRequest;
import com.project.Justick.Domain.SweetPotato.SweetPotato;
import com.project.Justick.Service.SweetPotato.SweetPotatoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sweetPotato")
public class SweetPotatoController extends AgriculturalController<SweetPotato, SweetPotatoRequest> {
    public SweetPotatoController(SweetPotatoService service) {
        super(service, "SweetPotato");
    }
}