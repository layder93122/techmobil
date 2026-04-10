package com.example.celulares.service;

import com.example.celulares.model.Celular;
import java.util.List;

public interface CelularService {
    // Nombre exacto: obtenerTodos
    List<Celular> obtenerTodos();

    void guardar(Celular celular);
    Celular obtenerPorId(Long id);
    void eliminar(Long id);
}