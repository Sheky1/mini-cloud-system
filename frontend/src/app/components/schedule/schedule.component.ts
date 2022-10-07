import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ErrorMessage } from 'src/app/model';
import { MachineService } from 'src/app/services/machine.service';

@Component({
  selector: 'app-schedule',
  templateUrl: './schedule.component.html',
  styleUrls: ['./schedule.component.css'],
})
export class ScheduleComponent implements OnInit {
  errorMessages: ErrorMessage[] = [];
  formattedMessages: any[] = [];

  constructor(private machineService: MachineService) {
    this.machineService.getErrorMessages().subscribe((data) => {
      this.errorMessages = data;
      this.errorMessages.forEach((element) => {
        this.formattedMessages.push({
          ...element,
          dateString: new Date(element.date).toLocaleString(),
        });
      });
    });
  }

  ngOnInit(): void {}
}
