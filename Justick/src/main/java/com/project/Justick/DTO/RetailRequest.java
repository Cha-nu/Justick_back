package com.project.Justick.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class RetailRequest {
    private int year;
    private int month;
    private int day;
    private int averagePrice;
    private int gap;
}
