import { PessoasService } from './../pessoas.service';
import { Component, OnInit } from '@angular/core';
import { Pessoa } from 'src/app/core/model';
import { MessageService } from 'primeng/api';
import { ErrorHandlerService } from 'src/app/core/error-handler.service';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-pessoas-cadastro',
  templateUrl: './pessoas-cadastro.component.html',
  styleUrls: ['./pessoas-cadastro.component.css'],
})
export class PessoasCadastroComponent {
  pessoa = new Pessoa();

  constructor(
    private pessoaService: PessoasService,
    private messageService: MessageService,
    private errorHandler: ErrorHandlerService,
    private route: ActivatedRoute,
    private router: Router,
    private title: Title,
  ) {}

  ngOnInit() {
    const idPessoa: number = this.route.snapshot.params['id'];

    if (idPessoa) {
      this.carregarPessoas(idPessoa);
      this.title.setTitle('Edição de pessoa');
    } else {
      this.title.setTitle('Nova pessoa');
    }

  }

  get edicao() {
    return Boolean(this.pessoa.id);
  }

  salvar(form: NgForm) {
    if (this.edicao) {
      this.atualizaPessoa();
    } else {
      this.adicionaPessoa();
    }
  }

  adicionaPessoa() {
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

  atualizaPessoa() {
    this.pessoaService
      .atualizar(this.pessoa)
      .then((pessoaAtualizada) => {
        if (pessoaAtualizada === undefined) return;

        this.pessoa = pessoaAtualizada;
        this.messageService.add({
          severity: 'success',
          detail: 'Pessoa editada com sucesso!',
        });
      })
      .catch((erro) => this.errorHandler.handle(erro));
  }

  carregarPessoas(id: number) {
    this.pessoaService
      .buscarPorId(id)
      .then((p) => {
        this.pessoa = p;
      })
      .catch((erro) => this.errorHandler.handle(erro));
  }

  novo(pessoaForm: NgForm) {
    pessoaForm.reset();

    setTimeout(() => {
      this.pessoa = new Pessoa();
    }, 1);

    this.router.navigate(['/pessoas/novo']);
  }
}
