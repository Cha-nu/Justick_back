package com.project.Justick.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class AgriculturalRequest {
    private int year;
    private int month;
    private int day;
    private int averagePrice; // 평균가격

}