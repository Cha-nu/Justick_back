package com.project.Justick.Controller.Cabbage;

import com.project.Justick.Controller.AgriculturalRetailController;
import com.project.Justick.DTO.Cabbage.CabbageRetailRequest;
import com.project.Justick.Domain.Cabbage.CabbageRetail;
import com.project.Justick.Domain.Potato.PotatoRetail;
import com.project.Justick.Service.Cabbage.CabbageRetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cabbage-retail")
public class CabbageRetailController extends AgriculturalRetailController<
        CabbageRetail,
        CabbageRetailRequest,
        CabbageRetailService> {

    public CabbageRetailController(CabbageRetailService service) {
        super(service, "CabbageRetail");
    }
}
