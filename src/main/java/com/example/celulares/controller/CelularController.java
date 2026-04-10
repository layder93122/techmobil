package com.example.celulares.controller;

import com.example.celulares.model.Celular;
import com.example.celulares.service.CelularService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;

@Controller
public class CelularController {

    @Autowired
    private CelularService celularService;

    // 1. LISTAR PRODUCTOS (Página principal)
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("lista", celularService.obtenerTodos());
        model.addAttribute("titulo", "NOVEDADES Y DESTACADOS");
        return "index";
    }

    // 2. MOSTRAR FORMULARIO PARA NUEVO PRODUCTO
    // Esta ruta soluciona el error 404 al hacer clic en "+ Vender"
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("celular", new Celular());
        return "formulario";
    }

    // 3. MOSTRAR FORMULARIO PARA EDITAR PRODUCTO EXISTENTE
    @GetMapping("/nuevo/{id}")
    public String editarCelular(@PathVariable Long id, Model model) {
        Celular celular = celularService.obtenerPorId(id);
        model.addAttribute("celular", celular);
        return "formulario";
    }

    // 4. GUARDAR O ACTUALIZAR PRODUCTO (Incluye manejo de imagen)
    @PostMapping("/guardar")
    public String guardar(Celular celular, @RequestParam("archivoImagen") MultipartFile archivo) {
        if (!archivo.isEmpty()) {
            String rutaAbsoluta = "D:\\celulares\\imagenes";
            try {
                Path directorio = Paths.get(rutaAbsoluta);
                if (!Files.exists(directorio)) {
                    Files.createDirectories(directorio);
                }

                byte[] bytesImg = archivo.getBytes();
                Path rutaCompleta = Paths.get(rutaAbsoluta + "\\" + archivo.getOriginalFilename());
                Files.write(rutaCompleta, bytesImg);

                // Guardamos solo el nombre del archivo en la base de datos
                celular.setImagen(archivo.getOriginalFilename());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        celularService.guardar(celular);
        return "redirect:/";
    }

    // 5. ELIMINAR PRODUCTO
    @PostMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        celularService.eliminar(id);
        return "redirect:/";
    }
}