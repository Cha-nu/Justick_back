package com.project.Justick.Controller.Tomato;

import com.project.Justick.Controller.AgriculturalRetailController;
import com.project.Justick.DTO.Tomato.TomatoRetailRequest;
import com.project.Justick.Domain.Tomato.TomatoRetail;
import com.project.Justick.Service.Tomato.TomatoRetailService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tomato-retail")
public class TomatoRetailController extends AgriculturalRetailController<
        TomatoRetail,
        TomatoRetailRequest,
        TomatoRetailService> {

    public TomatoRetailController(TomatoRetailService service) {
        super(service, "TomatoRetail");
    }
}
