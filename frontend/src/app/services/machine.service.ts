import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { ErrorMessage, Machine } from '../model';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root',
})
export class MachineService {
  private readonly mainApi = environment.mainApi;
  headers: any = {
    Authorization: `Bearer ${this.authService.getToken()}`,
    'Content-Type': 'application/json',
    Accept: 'application/json',
  };
  machines: Machine[] = [];
  activeRequsts: boolean[] = [];
  machinesIndices: number[] = [];
  processTime: number = 5000;
  scheduledRequests: any[] = [];
  requestErrors: any[] = [];

  constructor(
    private httpClient: HttpClient,
    private authService: AuthService
  ) {
    this.getMachines();
    this.checkSchedule();
  }

  loadAllMachines(): Observable<Machine[]> {
    let headers = this.headers;
    return this.httpClient.get<Machine[]>(
      `${this.mainApi}/machines/${this.authService.loggedUser.id}`,
      {
        headers,
      }
    );
  }

  getMachines = async () => {
    await this.loadAllMachines().subscribe((data) => {
      this.machines = data;
      this.machines.forEach((machine) => {
        this.activeRequsts.push(false);
        this.machinesIndices.push(machine.id);
      });
    });
  };

  createMachine(machine: Object) {
    let params = machine;
    let headers = this.headers;
    this.httpClient
      .post<Machine>(`${this.mainApi}/machines`, params, { headers })
      .subscribe((data) => console.log(data));
  }

  destroyMachine(id: string) {
    let headers = this.headers;
    this.httpClient
      .delete<Machine>(
        `${this.mainApi}/machines/${this.authService.loggedUser.id}/${id}`,
        { headers }
      )
      .subscribe((data) => console.log(data));
  }

  updateStatus(
    index: number,
    id: number,
    action: string,
    restartPhase: string
  ) {
    let updatedStatusObject = {
      id,
      userID: this.authService.loggedUser.id,
      action,
      restartPhase,
    };
    this.activeRequsts[index] = true;
    this.updateStatusApi(updatedStatusObject).subscribe(
      (machine) => {
        if (action == 'start' || action == 'stop') {
          setTimeout(() => {
            this.machines[index] = machine;
            this.activeRequsts[index] = false;
          }, this.processTime);
        } else if (action == 'restart' && restartPhase == 'first') {
          setTimeout(() => {
            this.machines[index] = machine;
            this.updateStatus(index, id, action, 'second');
          }, this.processTime / 2);
        } else if (action == 'restart' && restartPhase == 'second') {
          setTimeout(() => {
            this.machines[index] = machine;
            this.activeRequsts[index] = false;
          }, this.processTime / 2);
        }
      },
      (error) => {
        let requestError = {
          status: error.status,
          message: error.error.message,
          errorCode: error.error.errorCode,
          url: `${this.mainApi}/machines/update-status`,
          date: new Date().toLocaleDateString(),
          number: this.requestErrors.length,
        };
        this.requestErrors.push(requestError);
      }
    );
  }

  updateStatusApi(updateStatusObject: Object): Observable<Machine> {
    let params = updateStatusObject;
    let headers = this.headers;

    return this.httpClient.post<Machine>(
      `${this.mainApi}/machines/update-status`,
      params,
      {
        headers,
      }
    );
  }

  getErrorMessages(): Observable<ErrorMessage[]> {
    let headers = this.headers;
    return this.httpClient.get<ErrorMessage[]>(
      `${this.mainApi}/machines/error-messages`,
      {
        headers,
      }
    );
  }

  addScheduledRequest(request: any) {
    this.scheduledRequests.push(request);
  }

  checkSchedule() {
    setInterval(() => {
      console.log(this.scheduledRequests);
      let finishedRequests: number[] = [];
      this.scheduledRequests.forEach((request, i) => {
        if (request.date < new Date()) {
          console.log('Processing request number ', i, '...');
          console.log(request);
          let index = this.machinesIndices.indexOf(parseInt(request.id));
          let restartPhase =
            request.action == 'start' || request.action == 'stop'
              ? ''
              : 'first';
          this.updateStatus(
            index,
            parseInt(request.id),
            request.action,
            restartPhase
          );
          finishedRequests.push(i);
        }
      });
      for (var i = finishedRequests.length - 1; i >= 0; i--)
        this.scheduledRequests.splice(finishedRequests[i], 1);
    }, 5000);
  }
}
