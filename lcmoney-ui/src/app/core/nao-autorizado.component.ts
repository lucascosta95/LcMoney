
import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';

@Component({
  template: `
    <div class="container">
      <h1 class="text-center">Acesso Negado!</h1>
    <div>
  `,
  styles: [

  ]
})
export class NaoAutorizadoComponent implements OnInit {

  constructor(
    private title: Title
    ) { }

  ngOnInit(){
    this.title.setTitle('Acesso Negado');
  }

}
