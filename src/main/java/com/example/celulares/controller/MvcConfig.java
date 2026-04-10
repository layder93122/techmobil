package com.example.celulares.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Mapea la URL /imagenes/** a la carpeta física en el disco D
        registry.addResourceHandler("/imagenes/**")
                .addResourceLocations("file:/D:/celulares/imagenes/");
    }
}