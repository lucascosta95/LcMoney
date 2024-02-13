import { DatePipe } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class DashboardService {
  lancamentosUrl!: string;

  constructor(private http: HttpClient, private datePipe: DatePipe) {
    this.lancamentosUrl = `${environment.apiURL}/lancamentos`;
  }

  lancamentosPorCategoria(): Promise<Array<any>> {
    return this.http
      .get(`${this.lancamentosUrl}/estatisticas/por-categoria`)
      .toPromise()
      .then((response: any) => response);
  }

  lancamentosPorDia(): Promise<Array<any>> {
    return this.http
      .get(`${this.lancamentosUrl}/estatisticas/por-dia`)
      .toPromise()
      .then((response: any) => {
        const dados = response;
        this.converterStringsParaDatas(dados);

        return dados;
      });
  }

  private converterStringsParaDatas(dados: Array<any>) {
    for (const dado of dados) {
      let offset = new Date().getTimezoneOffset() * 60000;

      dado.dia = new Date(dado.dia);
      dado.dia = new Date(new Date(dado.dia).getTime() + offset);
    }
  }
}
