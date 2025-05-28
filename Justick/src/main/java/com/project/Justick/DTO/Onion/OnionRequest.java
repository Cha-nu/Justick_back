package com.project.Justick.DTO.Onion;

import com.project.Justick.DTO.AgriculturalRequest;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OnionRequest extends AgriculturalRequest {
    private int intake;
    private String grade; // 등급 SPECIAL, HIGH
}
