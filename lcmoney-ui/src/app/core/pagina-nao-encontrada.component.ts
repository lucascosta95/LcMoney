
import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';

@Component({
  selector: 'app-pagina-nao-encontrada',
  template: `
    <div class="container">
      <h1 class="text-center">Página não encontrada</h1>
    <div>
  `,
  styles: [

  ]
})
export class PaginaNaoEncontradaComponent implements OnInit {

  constructor(
    private title: Title
    ) { }

  ngOnInit(){
    this.title.setTitle('Edição de pessoa');
  }

}