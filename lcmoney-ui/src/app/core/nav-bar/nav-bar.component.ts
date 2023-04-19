import { Component, OnInit } from '@angular/core';
import { AuthService } from 'src/app/seguranca/auth.service';

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css'],
})
export class NavBarComponent implements OnInit {
  exibindoMenu: boolean = false;
  usuarioLogado: string = '';

  constructor(private auth: AuthService) {}

  ngOnInit() {
    this.usuarioLogado = this.auth.jwtPayload?.nome;
  }
}
