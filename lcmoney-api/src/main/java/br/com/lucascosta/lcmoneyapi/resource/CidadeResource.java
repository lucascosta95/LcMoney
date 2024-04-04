package br.com.lucascosta.lcmoneyapi.resource;

import br.com.lucascosta.lcmoneyapi.model.Cidade;
import br.com.lucascosta.lcmoneyapi.repository.CidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cidades")
public class CidadeResource {

    @Autowired
    private CidadeRepository cidadeRepository;

    @GetMapping()
    @PreAuthorize("isAuthenticated()")
    public List<Cidade> buscarPorEstadoId(@RequestParam Long estadoId) {
        return cidadeRepository.findByEstadoId(estadoId);
    }
}
