import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class CategoriaService {
  categoriasURL = 'http://localhost:8080/categorias';

  constructor(private http: HttpClient) {}

  listarTodas(): Promise<any> {
    return this.http.get(this.categoriasURL).toPromise();
  }
}
