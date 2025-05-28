package com.project.Justick.DTO.Onion;

import com.project.Justick.DTO.AgriculturalRequest;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OnionPredictRequest extends AgriculturalRequest {
    private String grade; // 등급 SPECIAL, HIGH
}
