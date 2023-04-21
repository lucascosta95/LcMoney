import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { JwtHelperService } from '@auth0/angular-jwt';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  oauthTokenURL!: string;
  tokenRevokeURL!: string;
  jwtPayload: any;

  constructor(
    private http: HttpClient, 
    private jwtHelper: JwtHelperService,
    ) {
    this.carregarToken();
    this.oauthTokenURL = `${environment.apiURL}/oauth/token`;
    this.tokenRevokeURL = `${environment.apiURL}/tokens/revoke`;
  }

  login(usuario: string, senha: string): Promise<void> {
    const headers = new HttpHeaders()
      .append('Content-Type', 'application/x-www-form-urlencoded')
      .append('Authorization', 'Basic YW5ndWxhcjpAbmd1bEByMA==');

    const body = `username=${usuario}&password=${senha}&grant_type=password`;

    return this.http
      .post(this.oauthTokenURL, body, { headers, withCredentials: true })
      .toPromise()
      .then((response: any) => {
        this.armazenarToken(response['access_token']);
      })
      .catch((response) => {
        if (response.status === 400) {
          if (response.error.error === 'invalid_grant') {
            return Promise.reject('Usuário ou senha inválida!');
          }
        }

        return Promise.reject(response);
      });
  }

  logout(){
    return this.http.delete(this.tokenRevokeURL, {withCredentials: true})
    .toPromise()
    .then(() => {
      this.limparAccessToken()
    })
  }

  private armazenarToken(token: string) {
    this.jwtPayload = this.jwtHelper.decodeToken(token);
    localStorage.setItem('token', token);
  }

  private carregarToken() {
    const token = localStorage.getItem('token');

    if (token) this.armazenarToken(token);
  }

  obterNovoAccessToken(): Promise<void> {
    const headers = new HttpHeaders()
      .append('Content-Type', 'application/x-www-form-urlencoded')
      .append('Authorization', 'Basic YW5ndWxhcjpAbmd1bEByMA==');

    const body = `grant_type=refresh_token`;

    return this.http
      .post(this.oauthTokenURL, body, { headers, withCredentials: true })
      .toPromise()
      .then((response: any) => {
        this.armazenarToken(response['access_token']);
      })
      .catch(() => {
        return Promise.resolve(undefined);
      });
  }

  isPermissao(permissao: string) {
    return this.jwtPayload && this.jwtPayload.authorities.includes(permissao);
  }

  isAccessTokenInvalido() {
    const token = localStorage.getItem('token');
    return !token || this.jwtHelper.isTokenExpired(token);
  }

  verificarPermissoes(roles: any) {

    for(const role of roles){
      if(this.isPermissao(role))
      return true;
    }

    return false;
  }

  limparAccessToken(){
    localStorage.removeItem('token');
    this.jwtPayload = undefined;
  }


}
