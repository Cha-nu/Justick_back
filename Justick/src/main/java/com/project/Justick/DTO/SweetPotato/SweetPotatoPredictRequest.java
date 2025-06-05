package com.project.Justick.DTO.SweetPotato;

import com.project.Justick.DTO.AgriculturalRequest;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SweetPotatoPredictRequest extends AgriculturalRequest {
    private String grade; // 등급 SPECIAL, HIGH
}
