import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from 'src/app/services/auth.service';
import { MachineService } from 'src/app/services/machine.service';

@Component({
  selector: 'app-destroy-machines',
  templateUrl: './destroy-machines.component.html',
  styleUrls: ['./destroy-machines.component.css'],
})
export class DestroyMachinesComponent implements OnInit {
  destroyMachineForm: FormGroup;

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private machineService: MachineService
  ) {
    this.authService.checkPermissions('can_delete_users');
    this.destroyMachineForm = this.formBuilder.group({
      id: ['', Validators.required],
    });
  }

  ngOnInit(): void {}

  destroyMachine() {
    this.machineService.destroyMachine(
      this.destroyMachineForm.get('id')?.value
    );
    this.destroyMachineForm.reset();
  }
}
