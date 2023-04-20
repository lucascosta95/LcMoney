import { MessageService } from 'primeng/api';
import { LancamentoService } from './../lancamento.service';
import { Lancamento } from './../../core/model';
import { PessoasService } from './../../pessoas/pessoas.service';
import { ErrorHandlerService } from './../../core/error-handler.service';
import { CategoriaService } from './../../categorias/categoria.service';
import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-lancamentos-cadastro',
  templateUrl: './lancamentos-cadastro.component.html',
  styleUrls: ['./lancamentos-cadastro.component.css'],
})
export class LancamentosCadastroComponent implements OnInit {
  constructor(
    private categoriaService: CategoriaService,
    private pessoasService: PessoasService,
    private lancamentoService: LancamentoService,
    private messageService: MessageService,
    private errorHandlerService: ErrorHandlerService,
    private route: ActivatedRoute,
    private router: Router,
    private title: Title,
  ) {}

  ngOnInit(): void {
    const idLancamento: number = this.route.snapshot.params['id'];

    if(idLancamento){
      this.carregarLancamento(idLancamento);
      this.title.setTitle('Edição de lançamento');
    } else {
      this.title.setTitle('Novo lançamento');
    }
    
    

    this.carregarCategorias();
    this.carregarPessoas();
  }

  tipos = [
    { label: "Receita", value: "RECEITA" },
    { label: "Despesa", value: "DESPESA" },
  ];

  categorias: any[] = [];
  pessoas: any[] = [];
  lancamento = new Lancamento();

  get edicao(){
    return Boolean(this.lancamento.id)
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

  salvar(lancamentoForm: NgForm){

    if(this.edicao){
      this.atualizarLancamento()
    } else {
      this.adicionarLancamento(lancamentoForm)
    }

  }

  carregarLancamento(id: number){
    this.lancamentoService.buscarPorCodigo(id).then(lancamento => {
      this.lancamento = lancamento
    }).catch(erro => this.errorHandlerService.handle(erro))
  }

  atualizarLancamento(){
    this.lancamentoService
      .atualizar(this.lancamento)
      .then((lanc) => {
        if (lanc === undefined) return;

        this.lancamento = lanc;

        this.messageService.add({
          severity: 'success',
          detail: 'Lançamento editado!',
        });
      })
      .catch((erro) => this.errorHandlerService.handle(erro));
  }


  adicionarLancamento(lancamentoForm: NgForm) {
    this.lancamentoService
      .adicionar(this.lancamento)
      .then(lancAdicionado => {
        this.messageService.add({
          severity: 'success',
          detail: 'Lançamento adicionado!',
        });

        this.router.navigate(['/lancamentos', lancAdicionado?.id])
      })
      .catch((erro) => this.errorHandlerService.handle(erro));
  }

  novo(lancamentoForm: NgForm){
    lancamentoForm.reset();

    setTimeout(() => {
      this.lancamento = new Lancamento();
    }, 1)
    
    this.router.navigate(['/lancamentos/novo'])
  }
}
