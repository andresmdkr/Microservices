package com.example.products.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El campo SKU no puede estar vacío")
    private String sku;

    @NotBlank(message = "El campo nombre no puede estar vacío")
    private String name;

    @NotNull(message = "El campo precio no puede ser nulo")
    @DecimalMin(value = "0.0", message = "El precio debe ser mayor o igual a cero")
    private double price;

    private Boolean status;
}

