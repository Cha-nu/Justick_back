package com.project.Justick.DTO.Tomato;

import com.project.Justick.DTO.AgriculturalRequest;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TomatoRequest extends AgriculturalRequest {
    private int intake;
    private String grade; // 등급 SPECIAL, HIGHTomatoRequest
}
