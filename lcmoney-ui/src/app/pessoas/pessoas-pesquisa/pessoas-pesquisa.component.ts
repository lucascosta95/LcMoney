import { ErrorHandlerService } from './../../core/error-handler.service';
import { PessoasService, PessoasFiltro } from './../pessoas.service';
import { Component, OnInit, ViewChild } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ConfirmationService, LazyLoadEvent, MessageService } from 'primeng/api';
import { Table } from 'primeng/table';

@Component({
  selector: 'app-pessoas-pesquisa',
  templateUrl: './pessoas-pesquisa.component.html',
  styleUrls: ['./pessoas-pesquisa.component.css'],
})
export class PessoasPesquisaComponent implements OnInit {
  pessoas = [];
  totalRegistros = 0;
  filtro = new PessoasFiltro();
  @ViewChild('tabela') grid!: Table;

  constructor(
    private pessoasService: PessoasService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private errorHandlerService: ErrorHandlerService,
    private title: Title,
  ) {}

  ngOnInit(): void {
    this.title.setTitle('Pesquisa de pessoas');
  }

  pesquisar(pagina = 0) {
    this.filtro.pagina = pagina;
    this.pessoasService.pesquisar(this.filtro).then((resultado) => {
      this.totalRegistros = resultado.total;
      this.pessoas = resultado.pessoas;
    }).catch(erro => this.errorHandlerService.handle(erro));
  }

  aoMudarPagina(event: LazyLoadEvent) {
    const pagina = event!.first! / event!.rows!;
    this.pesquisar(pagina);
  }

  confirmarExclusao(pessoa: any): void {
    this.confirmationService.confirm({
      message: 'Tem certeza que deseja excluir?',
      accept: () => {
        this.excluir(pessoa);
      },
    });
  }

  excluir(pessoa: any) {
    this.pessoasService.excluir(pessoa.id).then(() => {
      if (this.grid.first === 0) {
        this.pesquisar();
      } else {
        this.grid.reset();
      }

      this.messageService.add({
        severity: 'success',
        detail: 'Pessoa excluída!',
      });
    }).catch(erro => this.errorHandlerService.handle(erro));
  }

  mudarStatus(pessoa: any): void{
    const novoStatus = !pessoa.ativo;
    this.pessoasService.mudarStatus(pessoa.id, novoStatus).then(() => {

      if (this.grid.first === 0) {
        this.pesquisar();
      } else {
        this.grid.reset();
      }

      pessoa.ativo = novoStatus;

      this.messageService.add({
        severity: 'success',
        detail: 'Status Alterado com sucesso!',
      });
    }).catch(erro => this.errorHandlerService.handle(erro));
  }
}
