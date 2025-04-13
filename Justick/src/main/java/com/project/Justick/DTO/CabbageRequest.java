package com.project.Justick.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CabbageRequest {
    private int year;
    private int month;
    private int day;
    private int intake; // 반입량
    private int averagePrice; // 평균가격
    private int gap; // 가격차이
    private String grade;
}
