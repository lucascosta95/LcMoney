import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Pessoa } from '../core/model';

export class PessoasFiltro {
  nome?: string;
  pagina = 0;
  itensPorPagina = 5;
}

@Injectable({
  providedIn: 'root',
})
export class PessoasService {
  pessoasURL = 'http://localhost:8080/pessoas';

  constructor(private http: HttpClient) {}

  pesquisar(filtro: PessoasFiltro): Promise<any> {
    let params = new HttpParams();

    params = params.set('page', filtro.pagina);
    params = params.set('size', filtro.itensPorPagina);

    if (filtro.nome) {
      params = params.set('nome', filtro.nome);
    }

    return this.http
      .get(`${this.pessoasURL}`, { params })
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
    return this.http
      .get(this.pessoasURL)
      .toPromise()
      .then((response: any) => response['content']);
  }

  excluir(id: number): Promise<void> {
    return this.http
      .delete(`${this.pessoasURL}/${id}`)
      .toPromise()
      .then(() => undefined);
  }

  mudarStatus(id: number, ativo: boolean): Promise<void> {
    return this.http
      .put<void>(`${this.pessoasURL}/${id}/ativo`, ativo)
      .toPromise();
  }

  adicionar(pessoa: Pessoa) {
    return this.http.post<Pessoa>(this.pessoasURL, pessoa).toPromise();
  }

  atualizar(pessoa: Pessoa) {
    return this.http
      .put<Pessoa>(`${this.pessoasURL}/${pessoa.id}`, pessoa)
      .toPromise();
  }

  buscarPorId(id: Number): Promise<any> {
    return this.http
      .get(`${this.pessoasURL}/${id}`)
      .toPromise()
      .then((response: any) => response);
  }
}
