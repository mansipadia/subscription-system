package com.example.subscription.entity;

import com.example.subscription.enums.PlanType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull(message = "Name is Required...")
    private String name;

    @NotNull(message = "Price can not be null!")
    @DecimalMin(value = "0.0", inclusive = true)
    @Column(precision = 10,scale = 2)
    private BigDecimal price;

    @NotNull(message = "Duration days can not be null!")
    @Min(value = 1, message = "Duration days must be at least 1 day!")
    private Integer duration_days;

    @Lob
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PlanType tier;

    private Boolean active=true;

}
