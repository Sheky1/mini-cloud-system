export interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  permissions: [string];
}

export interface Token {
  token: string;
  user: User;
}

export interface Machine {
  id: number;
  name: string;
  status: string;
  user: User;
  createdBy: number;
}

export interface ErrorMessage {
  id: number;
  machineId: number;
  message: string;
  action: string;
  date: string;
}
