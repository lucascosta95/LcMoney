package br.com.lucascosta.lcmoneyapi.resource;

import br.com.lucascosta.lcmoneyapi.event.RecursoCriadoEvent;
import br.com.lucascosta.lcmoneyapi.model.Lancamento;
import br.com.lucascosta.lcmoneyapi.repository.LancamentoRepository;
import br.com.lucascosta.lcmoneyapi.repository.filter.LancamentoFilter;
import br.com.lucascosta.lcmoneyapi.repository.projection.ResumoLancamento;
import br.com.lucascosta.lcmoneyapi.service.LancamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Autowired
    private LancamentoService lancamentoService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and hasAuthority('SCOPE_read')")
    public Page<Lancamento> pesquisar(LancamentoFilter lancamentoFilter, Pageable pageable) {
        return lancamentoRepository.filtrar(lancamentoFilter, pageable);
    }

    @GetMapping(params = "resumo")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and hasAuthority('SCOPE_read')")
    public Page<ResumoLancamento> pesquisarResumido(LancamentoFilter lancamentoFilter, Pageable pageable) {
        return lancamentoRepository.resumir(lancamentoFilter, pageable);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and hasAuthority('SCOPE_read')")
    public ResponseEntity<Lancamento> buscarPorId(@PathVariable Long id) {
        Lancamento lancamento = lancamentoRepository.findById(id).orElse(null);
        return lancamento != null ? ResponseEntity.ok(lancamento) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO') and hasAuthority('SCOPE_write')")
    public ResponseEntity<Lancamento> criarLancamento(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {

        Lancamento lancamentoSalvo = lancamentoService.salvar(lancamento);
        publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSalvo.getId()));

        return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSalvo);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_REMOVER_LANCAMENTO') and hasAuthority('SCOPE_write')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerLancamento(@PathVariable Long id) {
        lancamentoRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_CADASTRAR_LANCAMENTO')")
    public ResponseEntity<Lancamento> atualizar(@PathVariable Long id, @Valid @RequestBody Lancamento lancamento) {
        try {
            Lancamento lancamentoSalvo = lancamentoService.atualizar(id, lancamento);
            return ResponseEntity.ok(lancamentoSalvo);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
