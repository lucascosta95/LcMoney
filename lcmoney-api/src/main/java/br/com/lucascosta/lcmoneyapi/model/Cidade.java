package br.com.lucascosta.lcmoneyapi.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "cidade")
public class Cidade {

    @Id
    private Long id;

    private String nome;

    @ManyToOne
    @JoinColumn(name = "id_estado")
    private Estado estado;
}
