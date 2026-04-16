package com.example.celulares.service;

import com.example.celulares.model.Celular;
import com.example.celulares.repository.CelularRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CelularServiceImpl implements CelularService {

    @Autowired
    private CelularRepository celularRepository;

    // ✔ Usa SOLO este método (consistente con tu proyecto)
    @Override
    public List<Celular> listarTodos() {
        return celularRepository.findAll();
    }

    @Override
    public void guardar(Celular celular) {
        celularRepository.save(celular);
    }

    @Override
    public Celular obtenerPorId(Long id) {
        return celularRepository.findById(id).orElse(null);
    }

    @Override
    public void eliminar(Long id) {
        celularRepository.deleteById(id);
    }
}