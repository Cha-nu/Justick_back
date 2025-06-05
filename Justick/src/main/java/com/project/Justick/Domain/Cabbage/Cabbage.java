package com.project.Justick.Domain.Cabbage;

import com.project.Justick.Domain.AgriculturalPrice;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter@Setter
public class Cabbage extends AgriculturalPrice {
    private int gap;
    private int intake;
}
