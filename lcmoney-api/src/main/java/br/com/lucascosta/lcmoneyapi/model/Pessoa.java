package br.com.lucascosta.lcmoneyapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "pessoa")
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String nome;
    @Embedded
    private Endereco endereco;
    @NotNull
    private Boolean ativo;

    @JsonIgnore
    @Transient
    public boolean isInativa() {
        return !this.ativo;
    }
}
