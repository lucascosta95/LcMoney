import { ErrorHandlerService } from './error-handler.service';
import { RouterLinkActive, RouterModule } from '@angular/router';
import { NgModule, LOCALE_ID } from '@angular/core';
import { CommonModule, DatePipe, registerLocaleData } from '@angular/common';
import { NavBarComponent } from './nav-bar/nav-bar.component';

import localePt from '@angular/common/locales/pt';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ToastModule } from 'primeng/toast';
import { LancamentoService } from '../lancamentos/lancamento.service';
import { TranslateService } from '@ngx-translate/core';
import { ConfirmationService, MessageService } from 'primeng/api';
import { PaginaNaoEncontradaComponent } from './pagina-nao-encontrada.component';
import { Title } from '@angular/platform-browser';
import { AuthService } from '../seguranca/auth.service';
import { JwtHelperService } from '@auth0/angular-jwt';
import { NaoAutorizadoComponent } from './nao-autorizado.component';
import { DashboardService } from '../dashboard/dashboard.service';
import { CategoriaService } from '../categorias/categoria.service';

registerLocaleData(localePt, 'pt-BR');

@NgModule({
  declarations: [
    NavBarComponent,
    PaginaNaoEncontradaComponent,
    NaoAutorizadoComponent,
  ],
  imports: [
    CommonModule,
    RouterModule,
    RouterLinkActive,
    ToastModule,
    ConfirmDialogModule,
  ],
  exports: [
    NavBarComponent,
    ToastModule,
    ConfirmDialogModule,
  ],
  providers: [
    ErrorHandlerService,
    AuthService,
    JwtHelperService,
    Title,
    LancamentoService,
    TranslateService,
    CategoriaService,
    DashboardService,
    MessageService, 
    ConfirmationService,
    DatePipe,
    {provide: LOCALE_ID, useValue: 'pt-BR' }
  ]
})
export class CoreModule { }