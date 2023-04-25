package br.com.lucascosta.lcmoneyapi.dto;

import br.com.lucascosta.lcmoneyapi.model.Categoria;

import java.math.BigDecimal;

public class LancamentosEstatisticaCategoria {
    private Categoria categoria;
    private BigDecimal total;

    public LancamentosEstatisticaCategoria(Categoria categoria, BigDecimal total) {
        this.categoria = categoria;
        this.total = total;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
