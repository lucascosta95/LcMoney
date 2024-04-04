package br.com.lucascosta.lcmoneyapi.repository;

import br.com.lucascosta.lcmoneyapi.model.Cidade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CidadeRepository extends JpaRepository<Cidade, Long> {
    List<Cidade> findByEstadoId(Long estadoId);
}
