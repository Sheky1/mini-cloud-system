import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from 'src/app/services/auth.service';
import { MachineService } from 'src/app/services/machine.service';

@Component({
  selector: 'app-create-machines',
  templateUrl: './create-machines.component.html',
  styleUrls: ['./create-machines.component.css'],
})
export class CreateMachinesComponent implements OnInit {
  createMachineForm: FormGroup;

  constructor(
    private machineService: MachineService,
    private authService: AuthService,
    private formBuilder: FormBuilder
  ) {
    this.authService.checkPermissions('can_create_machines');
    this.createMachineForm = this.formBuilder.group({
      name: ['', Validators.required],
    });
  }

  createMachine() {
    let machine = {
      name: this.createMachineForm.get('name')?.value,
      user: this.authService.loggedUser,
    };
    this.machineService.createMachine(machine);
    this.createMachineForm.reset();
  }

  ngOnInit(): void {}
}
