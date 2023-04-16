package br.com.lucascosta.lcmoneyapi.repository;

import br.com.lucascosta.lcmoneyapi.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
