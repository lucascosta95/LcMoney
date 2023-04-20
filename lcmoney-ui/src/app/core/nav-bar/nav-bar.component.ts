import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/seguranca/auth.service';
import { ErrorHandlerService } from '../error-handler.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css'],
})
export class NavBarComponent implements OnInit {
  exibindoMenu: boolean = false;
  usuarioLogado: string = '';

  constructor(
    private auth: AuthService,
    private error: ErrorHandlerService,
    private route: Router,
    ) {}

  ngOnInit() {
    this.usuarioLogado = this.auth.jwtPayload?.nome;
  }

  verificarPemissao(permissao: string){
    return this.auth.isPermissao(permissao);
  }

  logout(){
    this.auth.logout()
    .then(() => {
        this.route.navigate(['/login'])
    })
    .catch( erro => this.error.handle(erro))
  }

}
