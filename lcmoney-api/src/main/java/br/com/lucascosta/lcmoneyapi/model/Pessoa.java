package br.com.lucascosta.lcmoneyapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

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

    @JsonIgnoreProperties("pessoa")
    @Valid
    @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contato> contatos;

    @JsonIgnore
    @Transient
    public boolean isInativa() {
        return !this.ativo;
    }
}
