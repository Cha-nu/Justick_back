package com.project.Justick.Domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PredictPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "predict_price_id")
    private Long id;

    private String name;

    @Column(columnDefinition = "varchar(10)")
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(columnDefinition = "varchar(10)")
    @Enumerated(EnumType.STRING)
    private Grade grade;

    private int predictPrice;
}
