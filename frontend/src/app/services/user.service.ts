import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { User } from '../model';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private readonly mainApi = environment.mainApi;
  headers: any = {
    Authorization: `Bearer ${this.authService.getToken()}`,
    'Content-Type': 'application/json',
    Accept: 'application/json',
  };

  constructor(
    private httpClient: HttpClient,
    private authService: AuthService
  ) {}

  loadAllUsers(): Observable<User[]> {
    let headers = this.headers;
    return this.httpClient.get<User[]>(`${this.mainApi}/users`, {
      headers,
    });
  }

  createUser(user: Object) {
    let params = user;
    let headers = this.headers;
    this.httpClient
      .post<User>(`${this.mainApi}/users`, params, { headers })
      .subscribe((data) => console.log(data));
  }

  updateUser(user: Object, id: string) {
    let params = user;
    let headers = this.headers;
    this.httpClient
      .put<User>(`${this.mainApi}/users/${id}/update`, params, { headers })
      .subscribe((data) => console.log(data));
  }

  deleteUser(id: string) {
    let headers = this.headers;
    this.httpClient
      .delete<User>(`${this.mainApi}/users/${id}`, { headers })
      .subscribe((data) => console.log(data));
  }
}
