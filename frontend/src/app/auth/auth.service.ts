import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { JwtService } from '../common/jwt-service';
import { AuthRequest } from './dto/auth-request';
import { AuthResponse } from './dto/auth-response';
import { RegRequest } from './dto/reg-request';

@Injectable({
  providedIn: 'root'
})
export class AuthService extends JwtService {
  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { 
    super();
  }

  public authenticate(request: AuthRequest):  Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/authenticate`, request);
  }

  public register(request: RegRequest):  Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, request);
  }

}
