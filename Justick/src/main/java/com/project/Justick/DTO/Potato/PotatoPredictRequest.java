package com.project.Justick.DTO.Potato;

import com.project.Justick.DTO.AgriculturalRequest;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PotatoPredictRequest extends AgriculturalRequest {
    private String grade; // 등급 SPECIAL, HIGH
}
