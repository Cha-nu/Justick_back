package com.project.Justick.Domain.Tomato;

import com.project.Justick.Domain.AgriculturalPrice;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter@Setter
public class Tomato extends AgriculturalPrice {
    private int gap;
    private int intake;
}
