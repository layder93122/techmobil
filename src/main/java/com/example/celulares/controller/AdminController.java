package com.example.celulares.controller;

import com.example.celulares.model.Celular;
import com.example.celulares.service.CelularService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CelularService celularService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        // 🔹 Obtener todos los celulares UNA sola vez
        List<Celular> lista = celularService.listarTodos();

        // 🔹 Filtrar stock bajo (< 3)
        List<Celular> stockBajo = lista.stream()
                .filter(c -> c.getStock() < 3)
                .collect(Collectors.toList());

        // 🔹 Enviar datos a la vista
        model.addAttribute("alertasStock", stockBajo);
        model.addAttribute("totalProductos", lista.size());

        return "admin/dashboard";
    }
}