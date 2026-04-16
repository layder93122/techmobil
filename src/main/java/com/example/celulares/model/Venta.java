package com.example.celulares.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;
import java.util.List;

@Entity
@Data
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    private Double total;

    // Relación Muchos a Uno: Muchas ventas pueden pertenecer a un solo cliente
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    // Aquí podrías luego agregar una lista de 'DetalleVenta' para saber qué celulares se vendieron
}