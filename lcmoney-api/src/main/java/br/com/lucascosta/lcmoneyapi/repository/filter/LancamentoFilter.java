package br.com.lucascosta.lcmoneyapi.repository.filter;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class LancamentoFilter {
    private String descricao;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataVencimentoDe;
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataVencimentoAte;
}
