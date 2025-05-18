package com.project.Justick.Domain.Radish;

import com.project.Justick.Domain.AgriculturalPrice;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter@Setter
public class Radish extends AgriculturalPrice {
    private int gap;
}
