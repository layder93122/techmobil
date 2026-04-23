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

    // CONSTANTES PARA ELIMINAR CODE SMELLS
    public static final String CARRITO_KEY = "carrito";
    private static final String REDIRECT_HOME = "redirect:/";
    private static final String ATTR_ITEMS = "items";
    private static final String ATTR_TOTAL = "total";

    @Autowired
    private CelularService celularService;

    // Instancia de Random reutilizable para evitar crear múltiples objetos
    private final Random random = new Random();

    // 1. AGREGAR AL CARRITO
    @PostMapping("/agregar/{id}")
    public String agregar(@PathVariable Long id, HttpSession session) {
        Map<Long, Integer> carrito = (Map<Long, Integer>) session.getAttribute(CARRITO_KEY);
        if (carrito == null) {
            carrito = new HashMap<>();
        }

        carrito.put(id, carrito.getOrDefault(id, 0) + 1);
        session.setAttribute(CARRITO_KEY, carrito);

        // Actualizar totales en sesión
        session.setAttribute("totalItems", carrito.values().stream().mapToInt(i -> i).sum());

        List<Map<String, Object>> detallesSidebar = new ArrayList<>();
        double totalAcumulado = 0;

        for (var entry : carrito.entrySet()) {
            Celular c = celularService.obtenerPorId(entry.getKey());
            if (c != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("p", c);
                map.put("cant", entry.getValue());
                map.put("sub", c.getPrecio() * entry.getValue());
                detallesSidebar.add(map);
                totalAcumulado += c.getPrecio() * entry.getValue();
            }
        }

        session.setAttribute("carritoItems", detallesSidebar);
        session.setAttribute("totalPagar", totalAcumulado);

        return REDIRECT_HOME;
    }

    // 2. VER DETALLE DEL CARRITO
    @GetMapping("/ver")
    public String ver(HttpSession session, Model model) {
        Map<Long, Integer> carrito = (Map<Long, Integer>) session.getAttribute(CARRITO_KEY);
        List<Map<String, Object>> items = new ArrayList<>();
        double totalAcumulado = 0;

        if (carrito != null) {
            for (var entry : carrito.entrySet()) {
                Celular c = celularService.obtenerPorId(entry.getKey());
                if (c != null) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("p", c);
                    map.put("cant", entry.getValue());
                    map.put("sub", c.getPrecio() * entry.getValue());
                    items.add(map);
                    totalAcumulado += c.getPrecio() * entry.getValue();
                }
            }
        }
        model.addAttribute(ATTR_ITEMS, items);
        model.addAttribute(ATTR_TOTAL, totalAcumulado);
        return "carrito-detalle";
    }

    // 3. GENERAR BOLETA
    @GetMapping("/boleta")
    public String generarBoleta(HttpSession session, Model model) {
        Map<Long, Integer> carrito = (Map<Long, Integer>) session.getAttribute(CARRITO_KEY);

        if (carrito == null || carrito.isEmpty()) {
            return REDIRECT_HOME;
        }

        List<Map<String, Object>> detalles = new ArrayList<>();
        double totalAcumulado = 0;

        for (var entry : carrito.entrySet()) {
            Celular c = celularService.obtenerPorId(entry.getKey());
            if (c != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("p", c);
                map.put("cant", entry.getValue());
                map.put("sub", c.getPrecio() * entry.getValue());
                detalles.add(map);
                totalAcumulado += c.getPrecio() * entry.getValue();
            }
        }

        model.addAttribute(ATTR_ITEMS, detalles);
        model.addAttribute(ATTR_TOTAL, totalAcumulado);
        model.addAttribute("fecha", new java.util.Date());
        
        // SOLUCIÓN AL WARNING: Usamos nextInt(1000) en lugar de Math.random()
        model.addAttribute("nroBoleta", "BOL-00" + random.nextInt(1000));

        return "boleta";
    }

    // 4. FINALIZAR COMPRA
    @GetMapping("/finalizar")
    public String finalizarCompra(HttpSession session) {
        session.removeAttribute(CARRITO_KEY);
        session.removeAttribute("totalItems");
        session.removeAttribute("carritoItems");
        session.removeAttribute("totalPagar");
        return REDIRECT_HOME;
    }
}
