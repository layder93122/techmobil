package com.example.celulares.controller;

import com.example.celulares.model.Cliente;
import com.example.celulares.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/nuevo")
    public String formularioCliente(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "cliente-form";
    }

    @PostMapping("/guardar")
    public String guardarCliente(Cliente cliente) {
        clienteService.guardar(cliente);
        return "redirect:/clientes/lista";
    }

    @GetMapping("/lista")
    public String listar(Model model) {
        model.addAttribute("clientes", clienteService.listarTodos());
        return "cliente-lista";
    }

    // Funcionalidad de búsqueda rápida por DNI (útil para el carrito/boleta)
    @GetMapping("/buscar")
    @ResponseBody
    public Cliente buscar(@RequestParam String dni) {
        return clienteService.buscarPorDni(dni);
    }
}