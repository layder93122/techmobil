package com.example.celulares.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List; // <--- Asegúrate de tener este import

@Entity
@Data
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dni;
    private String nombre;
    private String telefono;
    private String direccion;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Venta> historialCompras;
}