import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CreateMachinesComponent } from './components/create-machines/create-machines.component';
import { CreateUserComponent } from './components/create-user/create-user.component';
import { DeleteUserComponent } from './components/delete-user/delete-user.component';
import { DestroyMachinesComponent } from './components/destroy-machines/destroy-machines.component';
import { LoginComponent } from './components/login/login.component';
import { ReadMachinesComponent } from './components/read-machines/read-machines.component';
import { ReadUsersComponent } from './components/read-users/read-users.component';
import { ScheduleComponent } from './components/schedule/schedule.component';
import { UpdateUserComponent } from './components/update-user/update-user.component';

const routes: Routes = [
  {
    path: '',
    component: LoginComponent,
  },
  {
    path: 'home',
    component: ReadUsersComponent,
  },
  {
    path: 'create-user',
    component: CreateUserComponent,
  },
  {
    path: 'update-user',
    component: UpdateUserComponent,
  },
  {
    path: 'delete-user',
    component: DeleteUserComponent,
  },
  {
    path: 'machines',
    component: ReadMachinesComponent,
  },
  {
    path: 'create-machine',
    component: CreateMachinesComponent,
  },
  {
    path: 'destroy-machine',
    component: DestroyMachinesComponent,
  },
  {
    path: 'schedule',
    component: ScheduleComponent,
  },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
