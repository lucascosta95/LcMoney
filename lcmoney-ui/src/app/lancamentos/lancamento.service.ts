import { DatePipe } from '@angular/common';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Lancamento } from '../core/model';
import { environment } from 'src/environments/environment';

export class LancamentosFiltro {
  descricao: string = '';
  dataVencimentoInicio?: Date;
  dataVencimentoFim?: Date;
  pagina = 0;
  itensPorPagina = 5;
}

@Injectable({
  providedIn: 'root',
})
export class LancamentoService {
  lancamentosURL!: string;

  constructor(private http: HttpClient, private datePipe: DatePipe) {
    this.lancamentosURL = `${environment.apiURL}/lancamentos`
  }

  pesquisar(filtro: LancamentosFiltro): Promise<any> {
    let params = new HttpParams();

    params = params.set('page', filtro.pagina);
    params = params.set('size', filtro.itensPorPagina);

    if (filtro.descricao) {
      params = params.set('descricao', filtro.descricao);
    }

    if (filtro.dataVencimentoInicio) {
      params = params.set(
        'dataVencimentoDe',
        this.datePipe.transform(filtro.dataVencimentoInicio, 'dd/MM/yyyy')!
      );
    }

    if (filtro.dataVencimentoFim) {
      params = params.set(
        'dataVencimentoAte',
        this.datePipe.transform(filtro.dataVencimentoFim, 'dd/MM/yyyy')!
      );
    }

    return this.http
      .get(`${this.lancamentosURL}?resumo`, { params })
      .toPromise()
      .then((response: any) => {
        const lancamentos = response['content'];

        const resultado = {
          lancamentos,
          total: response['totalElements'],
        };

        return resultado;
      });
  }

  excluir(id: number): Promise<void> {
    return this.http
      .delete(`${this.lancamentosURL}/${id}`)
      .toPromise()
      .then(() => undefined);
  }

  adicionar(lancamento: Lancamento) {
    const dataVencimentoFormatada = this.datePipe.transform(
      lancamento.dataVencimento,
      'dd/MM/yyyy'
    );
    const dataPagamentoFormatado = this.datePipe.transform(
      lancamento.dataPagamento,
      'dd/MM/yyyy'
    );

    const lancamentoFormatado = {
      ...lancamento,
      dataVencimento: dataVencimentoFormatada,
      dataPagamento: dataPagamentoFormatado,
    };

    return this.http
      .post<Lancamento>(this.lancamentosURL, lancamentoFormatado)
      .toPromise();
  }

  atualizar(lancamento: Lancamento) {
    const dataVencimentoFormatada = this.datePipe.transform(
      lancamento.dataVencimento,
      'dd/MM/yyyy'
    );
    const dataPagamentoFormatado = this.datePipe.transform(
      lancamento.dataPagamento,
      'dd/MM/yyyy'
    );

    const lancamentoFormatado = {
      ...lancamento,
      dataVencimento: dataVencimentoFormatada,
      dataPagamento: dataPagamentoFormatado,
    };

    return this.http
      .put<Lancamento>(
        `${this.lancamentosURL}/${lancamento.id}`,
        lancamentoFormatado
      )
      .toPromise();
  }

  buscarPorCodigo(id: number): Promise<Lancamento> {
    return this.http
      .get(`${this.lancamentosURL}/${id}`)
      .toPromise()
      .then((response: any) => {
        this.converterStringsParaDatas([response]);

        return response;
      });
  }

  private converterStringsParaDatas(lancamentos: Lancamento[]) {
    for (const lancamento of lancamentos) {
      if (lancamento.dataVencimento)
        lancamento.dataVencimento = this.converterStringToDate(
          lancamento.dataVencimento
        );

      if (lancamento.dataPagamento)
        lancamento.dataPagamento = this.converterStringToDate(
          lancamento.dataPagamento
        );
    }
  }

  private converterStringToDate(data: any) {
    const parts = data.split('/');
    return new Date(
      parseInt(parts[2]),
      parseInt(parts[1]) - 1,
      parseInt(parts[0])
    );
  }
}
