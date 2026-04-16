package com.example.celulares.service;

import com.example.celulares.model.Cliente;
import java.util.List;

public interface ClienteService {
    void guardar(Cliente cliente);
    List<Cliente> listarTodos();
    Cliente buscarPorDni(String dni);
    Cliente obtenerPorId(Long id);
}