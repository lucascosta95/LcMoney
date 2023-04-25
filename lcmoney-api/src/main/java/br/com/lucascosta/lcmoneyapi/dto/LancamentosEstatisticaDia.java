package br.com.lucascosta.lcmoneyapi.dto;

import br.com.lucascosta.lcmoneyapi.model.TipoLancamento;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LancamentosEstatisticaDia {

    private TipoLancamento tipoLancamento;
    private LocalDate dia;
    private BigDecimal total;

    public LancamentosEstatisticaDia(TipoLancamento tipoLancamento, LocalDate dia, BigDecimal total) {
        this.tipoLancamento = tipoLancamento;
        this.dia = dia;
        this.total = total;
    }

    public TipoLancamento getTipoLancamento() {
        return tipoLancamento;
    }

    public void setTipoLancamento(TipoLancamento tipoLancamento) {
        this.tipoLancamento = tipoLancamento;
    }

    public LocalDate getDia() {
        return dia;
    }

    public void setDia(LocalDate dia) {
        this.dia = dia;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
