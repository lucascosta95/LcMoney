package br.com.lucascosta.lcmoneyapi.dto;

import br.com.lucascosta.lcmoneyapi.model.TipoLancamento;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class LancamentosEstatisticaDia {

    private TipoLancamento tipoLancamento;
    private LocalDate dia;
    private BigDecimal total;

    public LancamentosEstatisticaDia(TipoLancamento tipoLancamento, LocalDate dia, BigDecimal total) {
        this.tipoLancamento = tipoLancamento;
        this.dia = dia;
        this.total = total;
    }
}
