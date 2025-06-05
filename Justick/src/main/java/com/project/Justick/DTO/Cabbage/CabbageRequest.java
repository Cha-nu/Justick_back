package com.project.Justick.DTO.Cabbage;

import com.project.Justick.DTO.AgriculturalRequest;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CabbageRequest extends AgriculturalRequest {
    private int intake;
    private String grade; // 등급 SPECIAL, HIGH
    private int gap;
}
