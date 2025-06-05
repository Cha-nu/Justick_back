package com.project.Justick.DTO.Tomato;

import com.project.Justick.DTO.AgriculturalRequest;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TomatoPredictRequest extends AgriculturalRequest {
    private String grade; // 등급 SPECIAL, HIGHTomatoRequest
}
