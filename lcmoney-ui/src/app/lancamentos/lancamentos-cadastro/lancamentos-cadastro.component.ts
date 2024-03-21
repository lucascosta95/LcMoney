import { MessageService } from 'primeng/api';
import { LancamentoService } from './../lancamento.service';
import { Lancamento } from './../../core/model';
import { PessoasService } from './../../pessoas/pessoas.service';
import { ErrorHandlerService } from './../../core/error-handler.service';
import { CategoriaService } from './../../categorias/categoria.service';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-lancamentos-cadastro',
  templateUrl: './lancamentos-cadastro.component.html',
  styleUrls: ['./lancamentos-cadastro.component.css'],
})
export class LancamentosCadastroComponent implements OnInit {
  formulario!: FormGroup;
  categorias: any[] = [];
  pessoas: any[] = [];
  uploadEmAndamento = false;

  tipos = [
    { label: 'Receita', value: 'RECEITA' },
    { label: 'Despesa', value: 'DESPESA' },
  ];

  constructor(
    private categoriaService: CategoriaService,
    private pessoasService: PessoasService,
    private lancamentoService: LancamentoService,
    private messageService: MessageService,
    private errorHandlerService: ErrorHandlerService,
    private route: ActivatedRoute,
    private router: Router,
    private title: Title,
    private formBuilder: FormBuilder
  ) {}

  ngOnInit(): void {
    this.configurarFormulario();

    const idLancamento: number = this.route.snapshot.params['id'];

    if (idLancamento) {
      this.carregarLancamento(idLancamento);
      this.title.setTitle('Edição de lançamento');
    } else {
      this.title.setTitle('Novo lançamento');
    }

    this.carregarCategorias();
    this.carregarPessoas();
  }

  configurarFormulario() {
    this.formulario = this.formBuilder.group({
      id: [],
      tipo: ['RECEITA', Validators.required],
      dataVencimento: [null, Validators.required],
      dataPagamento: [],
      descricao: [null, [Validators.required, Validators.minLength(5)]],
      valor: [null, Validators.required],

      categoria: this.formBuilder.group({
        id: [null, Validators.required],
        nome: [],
      }),

      pessoa: this.formBuilder.group({
        id: [null, Validators.required],
        nome: [],
      }),

      observacao: [],
      anexo: [],
      urlAnexo: [],
    });
  }

  get urlUploadAnexo() {
    return this.lancamentoService.urlUploadAnexo();
  }

  get uploadHeaders() {
    return this.lancamentoService.uploadHeaders();
  }

  get edicao() {
    return Boolean(this.formulario.get('id')!.value);
  }

  carregarCategorias() {
    return this.categoriaService
      .listarTodas()
      .then((categorias) => {
        this.categorias = categorias.map((c: any) => ({
          label: c.nome,
          value: c.id,
        }));
      })
      .catch((erro) => this.errorHandlerService.handle(erro));
  }

  carregarPessoas() {
    return this.pessoasService
      .listarTodas()
      .then((pessoas) => {
        this.pessoas = pessoas.map((p: any) => ({
          label: p.nome,
          value: p.id,
        }));
      })
      .catch((erro) => this.errorHandlerService.handle(erro));
  }

  antesUploadAnexo() {
    this.uploadEmAndamento = true;
  }

  aoTerminarUploadAnexo(event: any) {
    const anexo = event.originalEvent.body;
    this.formulario.patchValue({
      anexo: anexo.nome,
      urlAnexo: anexo.url.replace('\\\\', 'https://'),
    });
    this.uploadEmAndamento = false;
  }

  get nomeAnexo() {
    const nome = this.formulario?.get('anexo')?.value;
    if (nome) {
      return nome.substring(nome.indexOf('_') + 1, nome.length);
    }

    return '';
  }

  removerAnexo() {
    this.formulario.patchValue({
      anexo: null,
      urlAnexo: null,
    });
  }

  erroUpload(event: any) {
    this.uploadEmAndamento = false;
    this.messageService.add({
      severity: 'error',
      detail: 'Erro ao tentar enviar anexo!',
    });
  }

  salvar() {
    if (this.edicao) {
      this.atualizarLancamento();
    } else {
      this.adicionarLancamento();
    }
  }

  carregarLancamento(id: number) {
    this.lancamentoService
      .buscarPorCodigo(id)
      .then((lancamento) => {
        this.formulario.patchValue(lancamento);

        if (this.formulario.get('urlAnexo')?.value)
          this.formulario.patchValue({
            urlAnexo: this.formulario
              .get('urlAnexo')
              ?.value.replace('\\\\', 'https://'),
          });
      })
      .catch((erro) => this.errorHandlerService.handle(erro));
  }

  atualizarLancamento() {
    this.lancamentoService
      .atualizar(this.formulario.value)
      .then((lanc) => {
        if (lanc === undefined) return;

        this.formulario.patchValue(lanc);

        this.messageService.add({
          severity: 'success',
          detail: 'Lançamento editado!',
        });
      })
      .catch((erro) => this.errorHandlerService.handle(erro));
  }

  adicionarLancamento() {
    this.lancamentoService
      .adicionar(this.formulario.value)
      .then((lancAdicionado) => {
        this.messageService.add({
          severity: 'success',
          detail: 'Lançamento adicionado!',
        });

        this.router.navigate(['/lancamentos', lancAdicionado?.id]);
      })
      .catch((erro) => this.errorHandlerService.handle(erro));
  }

  novo() {
    this.formulario.reset();
    this.formulario.patchValue(new Lancamento());
    this.router.navigate(['/lancamentos/novo']);
  }
}
