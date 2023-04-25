package br.com.lucascosta.lcmoneyapi.repository.lancamento;

import br.com.lucascosta.lcmoneyapi.dto.LancamentosEstatisticaCategoria;
import br.com.lucascosta.lcmoneyapi.dto.LancamentosEstatisticaDia;
import br.com.lucascosta.lcmoneyapi.model.Lancamento;
import br.com.lucascosta.lcmoneyapi.repository.filter.LancamentoFilter;
import br.com.lucascosta.lcmoneyapi.repository.projection.ResumoLancamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface LancamentoRepositoryQuery {
    public Page<Lancamento> filtrar(LancamentoFilter lancamentoFilter, Pageable pageable);

    public Page<ResumoLancamento> resumir(LancamentoFilter lancamentoFilter, Pageable pageable);

    public List<LancamentosEstatisticaCategoria> porCategoria(LocalDate mesReferencia);
    public List<LancamentosEstatisticaDia> porDia(LocalDate mesReferencia);
}
