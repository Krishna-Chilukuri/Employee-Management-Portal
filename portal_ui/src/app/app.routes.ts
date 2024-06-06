import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { AddEmployeeComponent } from './add-employee/add-employee.component';
import { RemoveEmployeeComponent } from './remove-employee/remove-employee.component';
import { ViewAllEmployeesComponent } from './view-all-employees/view-all-employees.component';

export const routes: Routes = [
    {
        path: '',
        component: LoginComponent,
        title: "Login Page",
    },
    {
        path: 'home-component',
        component: HomeComponent,
        title: "Home Page",
    },
    {
        path: 'addEmployee',
        component: AddEmployeeComponent,
        title: "Add Employee",
    },
    {
        path: 'removeEmployee',
        component: RemoveEmployeeComponent,
        title: "Remove Employee",
    },
    {
        path: 'viewAllEmployee',
        component: ViewAllEmployeesComponent,
        title: "View Employees",
    }
];
