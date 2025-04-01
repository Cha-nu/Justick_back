package com.project.Justick.Domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class AgriculturalPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // id

    private LocalDate date; // 날짜
    private boolean rating; // 등급
    private int averagePrice; // 평균가격
}
