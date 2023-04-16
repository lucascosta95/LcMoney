import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Pessoa } from '../core/model';

export class PessoasFiltro{
  nome?: string;
  pagina = 0
  itensPorPagina = 5
}

@Injectable({
  providedIn: 'root',
})
export class PessoasService {
  pessoasURL = 'http://localhost:8080/pessoas';

  constructor(private http: HttpClient) {}

  pesquisar(filtro: PessoasFiltro): Promise<any> {
    const headers = new HttpHeaders().append(
      'Authorization',
      'Basic YWRtaW5AbGNtb25leS5jb206YWRtaW4='
    );
    let params = new HttpParams();

    params = params.set('page', filtro.pagina);
    params = params.set('size', filtro.itensPorPagina);

    if (filtro.nome) {
      params = params.set('nome', filtro.nome);
    }

    return this.http
      .get(`${this.pessoasURL}`, { headers, params })
      .toPromise()
      .then((response: any) => {
        const pessoas = response['content'];

        const resultado = {
          pessoas,
          total: response['totalElements'],
        };

        return resultado;
      });
  }

  listarTodas(): Promise<any> {
    const headers = new HttpHeaders().append(
      'Authorization',
      'Basic YWRtaW5AbGNtb25leS5jb206YWRtaW4='
    );

    return this.http
      .get(this.pessoasURL, { headers })
      .toPromise()
      .then((response: any) => response['content']);
  }

  excluir(id: number): Promise<void> {
    const headers = new HttpHeaders().append(
      'Authorization',
      'Basic YWRtaW5AbGNtb25leS5jb206YWRtaW4='
    );

    return this.http
      .delete(`${this.pessoasURL}/${id}`, { headers })
      .toPromise()
      .then(() => undefined);
  }

  mudarStatus(id: number, ativo: boolean): Promise<void> {
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic YWRtaW5AbGNtb25leS5jb206YWRtaW4=')
      .append('Content-Type', 'application/json');

    return this.http
      .put<void>(`${this.pessoasURL}/${id}/ativo`, ativo, { headers })
      .toPromise();
  }

  adicionar(pessoa: Pessoa) {
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic YWRtaW5AbGNtb25leS5jb206YWRtaW4=')
      .append('Content-Type', 'application/json');

    return this.http
      .post<Pessoa>(this.pessoasURL, pessoa, { headers })
      .toPromise();
  }

  atualizar(pessoa: Pessoa) {
    const headers = new HttpHeaders()
      .append('Authorization', 'Basic YWRtaW5AbGNtb25leS5jb206YWRtaW4=')
      .append('Content-Type', 'application/json');

    return this.http
      .put<Pessoa>(`${this.pessoasURL}/${pessoa.id}`, pessoa, { headers })
      .toPromise();
  }

  buscarPorId(id: Number): Promise<any> {
    const headers = new HttpHeaders().append(
      'Authorization',
      'Basic YWRtaW5AbGNtb25leS5jb206YWRtaW4='
    );

    return this.http
      .get(`${this.pessoasURL}/${id}`, { headers })
      .toPromise()
      .then((response: any) => response);
  }
}
