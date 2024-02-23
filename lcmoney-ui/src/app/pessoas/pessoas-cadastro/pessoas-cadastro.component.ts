import { PessoasService } from './../pessoas.service';
import { Component, OnInit } from '@angular/core';
import { Contato, Pessoa } from 'src/app/core/model';
import { MessageService } from 'primeng/api';
import { ErrorHandlerService } from 'src/app/core/error-handler.service';
import { NgForm } from '@angular/forms';

import { Title } from '@angular/platform-browser';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-pessoas-cadastro',
  templateUrl: './pessoas-cadastro.component.html',
})
export class PessoasCadastroComponent implements OnInit {
  pessoa = new Pessoa();

  constructor(
    private pessoaService: PessoasService,
    private messageService: MessageService,
    private errorHandler: ErrorHandlerService,
    private route: ActivatedRoute,
    private router: Router,
    private title: Title
  ) {}

  ngOnInit() {
    const idPessoa: number = this.route.snapshot.params['id'];

    if (idPessoa) {
      this.carregarPessoas(idPessoa);
    } else {
      this.title.setTitle('Nova pessoa');
    }
  }

  get edicao() {
    return Boolean(this.pessoa.id);
  }

  salvar() {
    if (this.edicao) {
      this.atualizarPessoa();
    } else {
      this.adicionarPessoa();
    }
  }

  adicionarPessoa() {
    this.pessoaService
      .adicionar(this.pessoa)
      .then(() => {
        this.messageService.add({
          severity: 'success',
          detail: 'Pessoa adicionada com sucesso!',
        });

        this.router.navigate(['/pessoas']);
      })
      .catch((erro) => this.errorHandler.handle(erro));
  }

  atualizarPessoa() {
    this.pessoaService
      .atualizar(this.pessoa)
      .then((pessoaAtualizada) => {
        if (pessoaAtualizada === undefined) return;

        this.pessoa = pessoaAtualizada;
        this.messageService.add({
          severity: 'success',
          detail: 'Pessoa editada com sucesso!',
        });

        this.atualizarTituloEdicao();
      })
      .catch((erro) => this.errorHandler.handle(erro));
  }

  carregarPessoas(id: number) {
    this.pessoaService
      .buscarPorId(id)
      .then((p) => {
        this.pessoa = p;
        this.atualizarTituloEdicao();
      })
      .catch((erro) => this.errorHandler.handle(erro));
  }

  novo(form: NgForm) {
    form.reset();

    setTimeout(() => {
      this.pessoa = new Pessoa();
    }, 1);

    this.router.navigate(['/pessoas/novo']);
  }

  atualizarTituloEdicao() {
    this.title.setTitle(`Edição de pessoa: ${this.pessoa.nome}`);
  }
}
