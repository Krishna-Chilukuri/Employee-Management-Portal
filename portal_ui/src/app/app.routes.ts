import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { HomeComponent } from './home/home.component';
import { AddEmployeeComponent } from './add-employee/add-employee.component';
import { RemoveEmployeeComponent } from './remove-employee/remove-employee.component';
import { ViewAllEmployeesComponent } from './view-all-employees/view-all-employees.component';
import { ViewEmployeeComponent } from './view-employee/view-employee.component';
import { PromoteEmployeeComponent } from './promote-employee/promote-employee.component';
import { DemoteEmployeeComponent } from './demote-employee/demote-employee.component';
import { HierarchiesComponent } from './hierarchies/hierarchies.component';

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
        title: "View All Employees",
    },
    {
        path: 'viewEmployee',
        component: ViewEmployeeComponent,
        title: "View Employee",
    },
    {
        path: 'promoteEmployee',
        component: PromoteEmployeeComponent,
        title: "Promote Employee",
    },
    {
        path: 'demoteEmployee',
        component: DemoteEmployeeComponent,
        title: "Demote Employee",
    },
    {
        path: 'hierarchies',
        component: HierarchiesComponent,
        title: "View Hierarchy",
    }
];
