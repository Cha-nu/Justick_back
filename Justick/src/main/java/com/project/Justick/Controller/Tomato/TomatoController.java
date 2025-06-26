package com.project.Justick.Controller.Tomato;


import com.project.Justick.Controller.AgriculturalController;
import com.project.Justick.DTO.Tomato.TomatoRequest;
import com.project.Justick.Domain.Tomato.Tomato;
import com.project.Justick.Service.Tomato.TomatoService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tomato")
public class TomatoController extends AgriculturalController<Tomato, TomatoRequest> {
    public TomatoController(TomatoService service) {
        super(service, "Tomato");
    }
}