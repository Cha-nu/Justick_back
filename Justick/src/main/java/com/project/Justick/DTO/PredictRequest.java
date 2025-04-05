package com.project.Justick.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PredictRequest {
    private String name;
    private String category;
    private String grade;
    private int predictPrice;
}
