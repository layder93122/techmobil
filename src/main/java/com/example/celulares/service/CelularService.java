package com.example.celulares.service;

import com.example.celulares.model.Celular;
import java.util.List;

public interface CelularService {

    // ✔ Método principal consistente con tu controller
    List<Celular> listarTodos();

    void guardar(Celular celular);

    Celular obtenerPorId(Long id);

    void eliminar(Long id);
}