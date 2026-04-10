package com.example.celulares.repository;

import com.example.celulares.model.Celular; // <--- ESTA ES LA LÍNEA QUE FALTA
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CelularRepository extends JpaRepository<Celular, Long> {
}