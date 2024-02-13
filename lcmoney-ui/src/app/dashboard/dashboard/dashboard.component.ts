import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { DashboardService } from '../dashboard.service';
import { DecimalPipe } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
})
export class DashboardComponent implements OnInit {
  constructor(
    private title: Title,
    private pipe: DecimalPipe,
    private dashboardService: DashboardService
  ) {}

  pieChartData: any;
  lineChartData: any;
  options = {
    tooltips: {
      callbacks: {
        label: (tooltipItem: any, data: any) => {
          const dataSet = data.datasets[tooltipItem.dataSetIndex];
          const valor = dataSet.data[tooltipItem.index];
          const label = dataSet.label ? `${dataSet.label}: ` : '';
          return `${label} ${this.pipe.transform(valor, '1.2-2')}`;
        },
      },
    },
  };

  ngOnInit() {
    this.title.setTitle('DashBoard');
    this.configurarGraficoPizza();
    this.configurarGraficoLinha();
  }

  configurarGraficoPizza() {
    this.dashboardService.lancamentosPorCategoria().then((dados) => {
      this.pieChartData = {
        labels: dados.map((dado) => dado.categoria.nome),
        datasets: [
          {
            data: dados.map((dado) => dado.total),
            backgroundColor: this.obterCoresDiferentes(dados.length),
          },
        ],
      };
    });
  }

  configurarGraficoLinha() {
    this.dashboardService.lancamentosPorDia().then((dados) => {
      const diasDoMes = this.configurarDiasMes();

      this.lineChartData = {
        labels: diasDoMes,
        datasets: [
          {
            label: 'Receitas',
            data: this.totaisPorCadaDiaMes(
              dados.filter((dado) => dado.tipoLancamento === 'RECEITA'),
              diasDoMes
            ),
            borderColor: '#3366CC',
          },
          {
            label: 'Despesas',
            data: this.totaisPorCadaDiaMes(
              dados.filter((dado) => dado.tipoLancamento === 'DESPESA'),
              diasDoMes
            ),
            borderColor: '#D62B00',
          },
        ],
      };
    });
  }

  private configurarDiasMes() {
    const dataAtual = new Date();
    const ultimoDiaDoMes = new Date(
      dataAtual.getFullYear(),
      dataAtual.getMonth() + 1,
      0
    ).getDate();
    const diasDoMes = Array.from(
      { length: ultimoDiaDoMes },
      (_, index) => index + 1
    );
    return diasDoMes;
  }

  private totaisPorCadaDiaMes(dados: any[], diasDoMes: number[]) {
    return diasDoMes.map((dia) => {
      const dadoCorrespondente = dados.find(
        (dado) => dado.dia.getDate() === dia
      );
      return dadoCorrespondente ? dadoCorrespondente.total : 0;
    });
  }

  obterCoresDiferentes(tamanhoArray: number): string[] {
    const cores: string[] = [];
    const passo = 360 / tamanhoArray;

    for (let i = 0; i < tamanhoArray; i++) {
      const cor = this.gerarCorContrastante(i * passo);
      cores.push(cor);
    }

    return cores;
  }

  gerarCorContrastante(hue: number): string {
    return `hsl(${hue}, 70%, 50%)`;
  }
}
