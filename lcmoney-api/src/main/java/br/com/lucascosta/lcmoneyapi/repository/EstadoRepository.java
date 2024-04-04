package br.com.lucascosta.lcmoneyapi.repository;

import br.com.lucascosta.lcmoneyapi.model.Estado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstadoRepository extends JpaRepository<Estado, Long> {
}
