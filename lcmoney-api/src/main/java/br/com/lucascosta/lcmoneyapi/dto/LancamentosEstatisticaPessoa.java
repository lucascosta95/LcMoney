package br.com.lucascosta.lcmoneyapi.dto;

import br.com.lucascosta.lcmoneyapi.model.Pessoa;
import br.com.lucascosta.lcmoneyapi.model.TipoLancamento;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LancamentosEstatisticaPessoa {
    private TipoLancamento tipo;
    private Pessoa pessoa;
    private BigDecimal total;

    public LancamentosEstatisticaPessoa(TipoLancamento tipo, Pessoa pessoa, BigDecimal total) {
        this.tipo = tipo;
        this.pessoa = pessoa;
        this.total = total;
    }
}
