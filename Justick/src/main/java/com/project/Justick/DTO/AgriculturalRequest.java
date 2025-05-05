package com.project.Justick.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AgriculturalRequest {
    private int year;
    private int month;
    private int day;
    private int intake; // 반입량
    private int averagePrice; // 평균가격
    private int gap; // 가격차이
    private String grade; // 등급 SPECIAL, HIGH
}