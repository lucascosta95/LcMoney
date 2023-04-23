import { PessoasService } from './../pessoas.service';
import { Component, OnInit } from '@angular/core';
import { Pessoa } from 'src/app/core/model';
import { MessageService } from 'primeng/api';
import { ErrorHandlerService } from 'src/app/core/error-handler.service';
import { FormBuilder, FormGroup, NgForm, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-pessoas-cadastro',
  templateUrl: './pessoas-cadastro.component.html',
  styleUrls: ['./pessoas-cadastro.component.css'],
})
export class PessoasCadastroComponent implements OnInit {
  formulario!: FormGroup;

  constructor(
    private pessoaService: PessoasService,
    private messageService: MessageService,
    private errorHandler: ErrorHandlerService,
    private route: ActivatedRoute,
    private router: Router,
    private title: Title,
    private formBuilder: FormBuilder
  ) {}

  ngOnInit() {
    this.configurarFormulario();

    const idPessoa: number = this.route.snapshot.params['id'];

    if (idPessoa) {
      this.carregarPessoas(idPessoa);
      this.title.setTitle('Edição de pessoa');
    } else {
      this.title.setTitle('Nova pessoa');
    }
  }

  configurarFormulario() {
    this.formulario = this.formBuilder.group({
      id: [],
      nome: [null, [Validators.required, Validators.minLength(5)]],
      ativo: [true],
      endereco: this.formBuilder.group({
        logradouro: [null, Validators.required],
        numero: [null, Validators.required],
        complemento: [],
        bairro: [null, Validators.required],
        cep: [null, Validators.required],
        cidade: [null, Validators.required],
        estado: [null, Validators.required],
      }),
    });
  }

  get edicao() {
    return Boolean(this.formulario.get('id')!.value);
  }

  salvar() {
    if (this.edicao) {
      this.atualizaPessoa();
    } else {
      this.adicionaPessoa();
    }
  }

  adicionaPessoa() {
    this.pessoaService
      .adicionar(this.formulario.value)
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
      .atualizar(this.formulario.value)
      .then((pessoaAtualizada) => {
        if (pessoaAtualizada === undefined) return;

        this.formulario.patchValue(pessoaAtualizada);
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
        this.formulario.patchValue(p);
      })
      .catch((erro) => this.errorHandler.handle(erro));
  }

  novo() {
    this.formulario.reset();
    this.formulario.patchValue(new Pessoa());
    this.router.navigate(['/pessoas/novo']);
  }
}
