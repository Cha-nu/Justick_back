package com.project.Justick.Domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@MappedSuperclass
@Getter @Setter
public abstract class AgriculturalPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "year_value")
    private int year;

    @Column(name = "month_value")
    private int month;

    @Column(name = "day_value")
    private int day;

    private int averagePrice;
    private int intake;
    private int gap;

    @Column(columnDefinition = "varchar(20)")
    @Enumerated(EnumType.STRING)
    private Grade grade;
}

