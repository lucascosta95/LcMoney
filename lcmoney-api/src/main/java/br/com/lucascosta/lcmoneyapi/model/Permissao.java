package br.com.lucascosta.lcmoneyapi.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "permissao")
public class Permissao {
    @Id
    private Long id;
    private String descricao;

}
