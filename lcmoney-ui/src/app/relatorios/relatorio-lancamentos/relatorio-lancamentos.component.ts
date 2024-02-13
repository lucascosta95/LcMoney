import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { RelatoriosService } from '../relatorios.service';

@Component({
  selector: 'app-relatorio-lancamentos',
  templateUrl: './relatorio-lancamentos.component.html',
})
export class RelatorioLancamentosComponent implements OnInit {
  periodoInicio!: any;
  periodoFim!: any;

  constructor(
    private title: Title,
    private relatoriosService: RelatoriosService
  ) {}

  ngOnInit(): void {
    this.title.setTitle('Relatório de Lancamentos');
  }

  gerar() {
    this.relatoriosService
      .relatorioLancamentosPorPessoa(this.periodoInicio, this.periodoFim)
      .then((relatorio) => {
        window.open(window.URL.createObjectURL(relatorio!));
      });
  }
}
