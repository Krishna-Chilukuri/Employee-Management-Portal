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
import { PromoteToOwnerComponent } from './promote-to-owner/promote-to-owner.component';
import { PromoteToAdminComponent } from './promote-to-admin/promote-to-admin.component';
import { DemoteOwnerComponent } from './demote-owner/demote-owner.component';
import { RemoveAdminOwnerComponent } from './remove-admin-owner/remove-admin-owner.component';
import { ViewAdminOwnerComponent } from './view-admin-owner/view-admin-owner.component';
import { RouteGuardService, canAdminNavigate, canNavigate, canOwnerNavigate, canPrivNavigate } from './route-guard.service';

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
        canActivate: [canNavigate],
    },
    {
        path: 'addEmployee',
        component: AddEmployeeComponent,
        title: "Add Employee",
        canActivate: [canPrivNavigate],
    },
    {
        path: 'removeEmployee',
        component: RemoveEmployeeComponent,
        title: "Remove Employee",
        canActivate: [canPrivNavigate],
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
        canActivate: [canPrivNavigate],
    },
    {
        path: 'demoteEmployee',
        component: DemoteEmployeeComponent,
        title: "Demote Employee",
        canActivate: [canPrivNavigate],
    },
    {
        path: 'hierarchies',
        component: HierarchiesComponent,
        title: "View Hierarchy",
    },
    {
        path: 'promoteToOwner',
        component: PromoteToOwnerComponent,
        title: "Promote To Owner",
        canActivate: [canOwnerNavigate],
    },
    {
        path: 'promoteToAdmin',
        component: PromoteToAdminComponent,
        title: "Promote To Admin",
        canActivate: [canOwnerNavigate],
    },
    {
        path: 'demoteOwner',
        component: DemoteOwnerComponent,
        title: "Demote To Owner",
        canActivate: [canOwnerNavigate],
    },
    {
        path: 'removeAdminOwner',
        component: RemoveAdminOwnerComponent,
        title: "Remove Admin/Owner",
        canActivate: [canOwnerNavigate],
    },
    {
        path: 'viewAdminOwner',
        component: ViewAdminOwnerComponent,
        title: "View Admin & Owner",
        canActivate: [canAdminNavigate],
    }
];
