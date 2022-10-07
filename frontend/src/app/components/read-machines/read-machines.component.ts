import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Machine } from 'src/app/model';
import { AuthService } from 'src/app/services/auth.service';
import { MachineService } from 'src/app/services/machine.service';

@Component({
  selector: 'app-read-machines',
  templateUrl: './read-machines.component.html',
  styleUrls: ['./read-machines.component.css'],
})
export class ReadMachinesComponent implements OnInit {
  scheduleForm: FormGroup;
  machines: Machine[] = [];
  activeRequsts: boolean[] = [];

  constructor(
    private authService: AuthService,
    private machineService: MachineService,
    private formBuilder: FormBuilder
  ) {
    this.scheduleForm = this.formBuilder.group({
      date: ['', Validators.required],
      time: ['', Validators.required],
      id: ['', Validators.required],
      actionGroup: ['', Validators.required],
    });
    this.authService.checkPermissions('can_search_machines');
    setTimeout(() => {
      this.machines = this.machineService.machines;
      this.activeRequsts = this.machineService.activeRequsts;
    }, 300);
  }

  hasPermission(permission: string, action: string, index: number) {
    let allowed = true;
    if (action == 'start' && !(this.machines[index].status == 'STOPPED'))
      allowed = false;
    else if (
      (action == 'stop' || action == 'restart') &&
      !(this.machines[index].status == 'RUNNING')
    )
      allowed = false;
    return (
      this.authService.hasPermission(permission) &&
      !this.activeRequsts[index] &&
      allowed
    );
  }

  hasPermissionAuth(permission: string) {
    return this.authService.hasPermission(permission);
  }

  hasAnyPermissionAuth() {
    return (
      this.authService.hasPermission('can_start_machines') ||
      this.authService.hasPermission('can_stop_machines') ||
      this.authService.hasPermission('can_restart_machines')
    );
  }

  getMachines() {
    this.machineService.loadAllMachines().subscribe((data) => {
      this.machines = data;
    });
  }

  updateStatus(
    index: number,
    id: number,
    action: string,
    restartPhase: string
  ) {
    this.machineService.updateStatus(index, id, action, restartPhase);
  }

  schedule() {
    let inputValue = this.scheduleForm.get('time')?.value;
    let values: string[] = inputValue.split(' ');
    let time = inputValue.split(' ')[0].split(':');
    let date = new Date(this.scheduleForm.get('date')?.value);
    if (values[1] == 'AM') date.setHours(parseInt(time[0]));
    else date.setHours(parseInt(time[0]) + 12);
    date.setMinutes(parseInt(time[1]));
    date.setSeconds(0);
    let request = {
      date,
      action: this.scheduleForm.get('actionGroup')?.value,
      id: this.scheduleForm.get('id')?.value,
    };
    this.machineService.addScheduledRequest(request);
  }

  ngOnInit(): void {}
}
