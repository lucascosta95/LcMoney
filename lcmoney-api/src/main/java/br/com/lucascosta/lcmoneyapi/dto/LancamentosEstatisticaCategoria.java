package br.com.lucascosta.lcmoneyapi.dto;

import br.com.lucascosta.lcmoneyapi.model.Categoria;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class LancamentosEstatisticaCategoria {
    private Categoria categoria;
    private BigDecimal total;

    public LancamentosEstatisticaCategoria(Categoria categoria, BigDecimal total) {
        this.categoria = categoria;
        this.total = total;
    }
}
