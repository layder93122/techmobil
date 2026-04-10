package com.example.celulares.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data // <--- Esta anotación ya crea todos los Getters, Setters y el Constructor
public class Celular {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String marca;
    private String modelo;
    private Double precio;
    private Integer stock;
    private String imagen;

    // NOTA: He borrado los Getters y Setters manuales porque @Data ya los hace por ti.
}