package com.project.Justick.DTO.Cabbage;

import com.project.Justick.DTO.AgriculturalRequest;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CabbagePredictRequest extends AgriculturalRequest {
    private String grade; // 등급 SPECIAL, HIGH
}
