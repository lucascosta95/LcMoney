package br.com.lucascosta.lcmoneyapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Anexo {
    private String nome;
    private String url;

    public Anexo(String nome, String url) {
        this.nome = nome;
        this.url = url;
    }
}
