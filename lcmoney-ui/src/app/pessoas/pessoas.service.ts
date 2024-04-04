import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Cidade, Estado, Pessoa } from '../core/model';
import { environment } from 'src/environments/environment';

export class PessoasFiltro {
  nome?: string;
  pagina = 0;
  itensPorPagina = 5;
}

@Injectable({
  providedIn: 'root',
})
export class PessoasService {
  pessoasUrl!: string;
  cidadesUrl!: string;
  estadosUrl!: string;

  constructor(private http: HttpClient) {
    this.pessoasUrl = `${environment.apiURL}/pessoas`;
    this.cidadesUrl = `${environment.apiURL}/cidades`;
    this.estadosUrl = `${environment.apiURL}/estados`;
  }

  pesquisar(filtro: PessoasFiltro): Promise<any> {
    let params = new HttpParams();

    params = params.set('page', filtro.pagina);
    params = params.set('size', filtro.itensPorPagina);

    if (filtro.nome) {
      params = params.set('nome', filtro.nome);
    }

    return this.http
      .get(`${this.pessoasUrl}`, { params })
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
      .get(this.pessoasUrl)
      .toPromise()
      .then((response: any) => response['content']);
  }

  excluir(id: number): Promise<void> {
    return this.http
      .delete(`${this.pessoasUrl}/${id}`)
      .toPromise()
      .then(() => undefined);
  }

  mudarStatus(id: number, ativo: boolean): Promise<void> {
    return this.http
      .put<void>(`${this.pessoasUrl}/${id}/ativo`, ativo)
      .toPromise();
  }

  adicionar(pessoa: Pessoa) {
    return this.http.post<Pessoa>(this.pessoasUrl, pessoa).toPromise();
  }

  atualizar(pessoa: Pessoa) {
    return this.http
      .put<Pessoa>(`${this.pessoasUrl}/${pessoa.id}`, pessoa)
      .toPromise();
  }

  buscarPorId(id: Number): Promise<any> {
    return this.http
      .get(`${this.pessoasUrl}/${id}`)
      .toPromise()
      .then((response: any) => response);
  }

  listarEstados(): Promise<Estado[]> {
    return <Promise<Estado[]>>this.http.get(this.estadosUrl).toPromise();
  }

  pesquisarCidades(estadoId: number): Promise<Cidade[]> {
    const params = new HttpParams().set('estadoId', estadoId);

    return <Promise<Cidade[]>>(
      this.http.get(this.cidadesUrl, { params }).toPromise()
    );
  }
}
