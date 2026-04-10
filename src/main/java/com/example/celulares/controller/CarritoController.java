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

    @Autowired
    private CelularService celularService;

    // 1. AGREGAR AL CARRITO (Actualizado para el Sidebar)
    @PostMapping("/agregar/{id}")
    public String agregar(@PathVariable Long id, HttpSession session) {
        Map<Long, Integer> carrito = (Map<Long, Integer>) session.getAttribute("carrito");
        if (carrito == null) carrito = new HashMap<>();

        // Actualizar el conteo en el mapa
        carrito.put(id, carrito.getOrDefault(id, 0) + 1);
        session.setAttribute("carrito", carrito);

        // Actualizar cantidad total de items
        session.setAttribute("totalItems", carrito.values().stream().mapToInt(i -> i).sum());

        // --- LÓGICA PARA ACTUALIZAR EL SIDEBAR EN TIEMPO REAL ---
        List<Map<String, Object>> detallesSidebar = new ArrayList<>();
        double total = 0;

        for (var entry : carrito.entrySet()) {
            Celular c = celularService.obtenerPorId(entry.getKey());
            if (c != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("p", c);
                map.put("cant", entry.getValue());
                // Calculamos el subtotal para que se vea en el sidebar si lo deseas
                map.put("sub", c.getPrecio() * entry.getValue());
                detallesSidebar.add(map);
                total += c.getPrecio() * entry.getValue();
            }
        }

        // Guardamos en la sesión para que el index.html lo lea de session.carritoItems
        session.setAttribute("carritoItems", detallesSidebar);
        session.setAttribute("totalPagar", total);

        return "redirect:/";
    }

    // 2. VER DETALLE DEL CARRITO (Página completa)
    @GetMapping("/ver")
    public String ver(HttpSession session, Model model) {
        Map<Long, Integer> carrito = (Map<Long, Integer>) session.getAttribute("carrito");
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

    // 3. GENERAR BOLETA DE VENTA
    @GetMapping("/boleta")
    public String generarBoleta(HttpSession session, Model model) {
        Map<Long, Integer> carrito = (Map<Long, Integer>) session.getAttribute("carrito");

        if (carrito == null || carrito.isEmpty()) {
            return "redirect:/";
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

    // 4. FINALIZAR COMPRA (Limpiar todo)
    @GetMapping("/finalizar")
    public String finalizarCompra(HttpSession session) {
        session.removeAttribute("carrito");
        session.removeAttribute("totalItems");
        session.removeAttribute("carritoItems");
        session.removeAttribute("totalPagar");
        return "redirect:/";
    }
}