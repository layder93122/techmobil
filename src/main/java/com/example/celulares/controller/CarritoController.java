package com.example.celulares.controller;

import com.example.celulares.model.Celular;
import com.example.celulares.service.CelularService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    // 1. Constantes para evitar duplicados según SonarCloud
    public static final String CARRITO_KEY = "carrito";
    private static final String REDIRECT_HOME = "redirect:/";

    @Autowired
    private CelularService celularService;

    @PostMapping("/agregar/{id}")
    public String agregar(@PathVariable Long id, HttpSession session) {
        Map<Long, Integer> carrito = (Map<Long, Integer>) session.getAttribute(CARRITO_KEY);
        if (carrito == null) {
            carrito = new HashMap<>();
        }

        carrito.put(id, carrito.getOrDefault(id, 0) + 1);
        session.setAttribute(CARRITO_KEY, carrito);

        session.setAttribute("totalItems", carrito.values().stream().mapToInt(i -> i).sum());

        List<Map<String, Object>> detallesSidebar = new ArrayList<>();
        double total = 0;

        for (var entry : carrito.entrySet()) {
            Celular c = celularService.obtenerPorId(entry.getKey());
            if (c != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("p", c);
                map.put("cant", entry.getValue());
                map.put("sub", c.getPrecio() * entry.getValue());
                detallesSidebar.add(map);
                total += c.getPrecio() * entry.getValue();
            }
        }

        session.setAttribute("carritoItems", detallesSidebar);
        session.setAttribute("totalPagar", total);

        return REDIRECT_HOME; // Usamos la constante aquí
    }

    @GetMapping("/ver")
    public String ver(HttpSession session, Model model) {
        Map<Long, Integer> carrito = (Map<Long, Integer>) session.getAttribute(CARRITO_KEY);
        List<Map<String, Object>> items = new ArrayList<>();
        double total = 0;

        if (carrito != null) {
            for (var entry : carrito.entrySet()) {
                Celular c = celularService.obtenerPorId(entry.getKey());
                if (c != null) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("p", c);
                    map.put("cant", entry.getValue());
                    map.put("sub", c.getPrecio() * entry.getValue());
                    items.add(map);
                    total += c.getPrecio() * entry.getValue();
                }
            }
        }
        model.addAttribute("items", items);
        model.addAttribute("total", total);
        return "carrito-detalle";
    }

    @GetMapping("/boleta")
    public String generarBoleta(HttpSession session, Model model) {
        Map<Long, Integer> carrito = (Map<Long, Integer>) session.getAttribute(CARRITO_KEY);

        if (carrito == null || carrito.isEmpty()) {
            return REDIRECT_HOME; // Usamos la constante aquí
        }

        List<Map<String, Object>> detalles = new ArrayList<>();
        double total = 0;

        for (var entry : carrito.entrySet()) {
            Celular c = celularService.obtenerPorId(entry.getKey());
            if (c != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("p", c);
                map.put("cant", entry.getValue());
                map.put("sub", c.getPrecio() * entry.getValue());
                detalles.add(map);
                total += c.getPrecio() * entry.getValue();
            }
        }

        model.addAttribute("items", detalles);
        model.addAttribute("total", total);
        model.addAttribute("fecha", new java.util.Date());
        model.addAttribute("nroBoleta", "BOL-00" + (int)(Math.random() * 1000));

        return "boleta";
    }

    @GetMapping("/finalizar")
    public String finalizarCompra(HttpSession session) {
        session.removeAttribute(CARRITO_KEY);
        session.removeAttribute("totalItems");
        session.removeAttribute("carritoItems");
        session.removeAttribute("totalPagar");
        return REDIRECT_HOME; // Usamos la constante aquí
    }
}
