package com.project.Justick.Domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CabbagePriceRequest {
    private String date;     // "yyyy-MM-dd" 형식 문자열
    private int rating;      // 0 or 1
    private int averagePrice;
}
