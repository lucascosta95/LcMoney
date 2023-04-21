import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class CategoriaService {
  categoriasURL!: string;

  constructor(private http: HttpClient) {
    this.categoriasURL = `${environment.apiURL}/categorias`
  }

  listarTodas(): Promise<any> {
    return this.http.get(this.categoriasURL).toPromise();
  }
}
