package com.example.orders.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "orders")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El campo productId no puede estar vacío")
    private Long productId;

    @NotNull(message = "El campo unitPrice no puede estar vacío")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio unitario debe ser mayor que 0")
    private double unitPrice;

    @NotNull(message = "El campo quantity no puede ser nulo")
    @Min(value = 1, message = "La cantidad debe ser mayor a cero")
    private Long quantity;

    @NotNull(message = "El campo total no puede estar vacío")
    @DecimalMin(value = "0.0", inclusive = false, message = "El total debe ser mayor que 0")
    private double total;

    @NotNull(message = "El campo date no puede estar vacío")
    private LocalDate date;

    @NotBlank(message = "El campo notes no puede estar vacío")
    private String notes;
}
