import { ErrorHandlerService } from 'src/app/core/error-handler.service';
import { Component} from '@angular/core';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login-form',
  templateUrl: './login-form.component.html',
  styleUrls: ['./login-form.component.css']
})
export class LoginFormComponent {

  constructor(
    private auth: AuthService,
    private errorHandlerService: ErrorHandlerService,
    private router: Router,

    ) { }


  realizarLogin(usuario: string , senha: string){
    this.auth.login(usuario, senha)
    .then(() => {
      this.router.navigate(['/dashboard'])})
    .catch(erro => {
      this.errorHandlerService.handle(erro);
    })
  }

}
