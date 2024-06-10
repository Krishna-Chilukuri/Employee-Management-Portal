package com.employee.portal.controller;

import com.employee.portal.factory.Logger;
import com.employee.portal.model.Employee;
import com.employee.portal.model.Login;
import com.employee.portal.model.Reportee;
import com.employee.portal.service.EmployeeService;
import com.employee.portal.service.implementation.EmployeeServiceImplementation;
import com.employee.portal.service.implementation.LoginServiceImplementation;

import com.employee.portal.service.implementation.ReporteeServiceImplementation;
import jakarta.persistence.GeneratedValue;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Random;

@CrossOrigin(origins = "http://localhost:4200/")
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeServiceImplementation employeeServiceImpl;
    private final LoginServiceImplementation loginServiceImplementation;
    private final ReporteeServiceImplementation reporteeServiceImplementation;
    public EmployeeController(EmployeeServiceImplementation employeeServiceImpl, LoginServiceImplementation loginServiceImplementation, ReporteeServiceImplementation reporteeServiceImplementation) {
        super();
        this.employeeServiceImpl = employeeServiceImpl;
        this.loginServiceImplementation = loginServiceImplementation;
        this.reporteeServiceImplementation = reporteeServiceImplementation;
    }

    @RequestMapping("/save")
    public ResponseEntity<Employee> saveEmployee(@RequestBody Employee employee) throws IOException {
        Logger lg = Logger.getInstance();
        lg.log("Emp received: " + employee);
        if (!isRankPossible(employee.getEmployeeRank())) {
            lg.log("RANK MAX CAP REACHED");
            return new ResponseEntity<Employee>(new Employee(), HttpStatus.CONFLICT);
        }
        employee = addtoHierarchy(employee);
        lg.log("EMPLOYEE CAN BE INSERTED");
        Login logUser = new Login();
        // employee = employeeServiceImpl.saveEmployee(employee);
        logUser.setUsername(String.valueOf(employee.getEmployeeId()));
        logUser.setPassword(employee.getEmployeeName());
        logUser.setPrivilege("priv_user");
        logUser = loginServiceImplementation.saveLogin(logUser);
        lg.log("LOGIN CREATED : " + logUser);
        return new ResponseEntity<Employee>(employee, HttpStatus.CREATED);
    }

    @RequestMapping("/delete")
    public void deleteEmployee(@RequestParam(name = "empId") long empId) throws IOException {
        Logger lg = Logger.getInstance();
        lg.log("Delete Request received for " + empId);
        employeeServiceImpl.deleteEmployee(empId);
//        return new ResponseEntity<Employee>(new Employee(), HttpStatus.OK);
    }

    @RequestMapping("/view")
    public ResponseEntity<viewableEmployee> viewEmployee(@RequestParam(name = "empId") long empId) throws IOException {
        Logger lg = Logger.getInstance();
        viewableEmployee retEmp = new viewableEmployee();

        retEmp.setEmployee(employeeServiceImpl.getEmployeeById(empId));
        retEmp.setReportees(reporteeServiceImplementation.getReporteesById(empId));
        lg.log("View Employee request received for " + empId + retEmp);
        return new ResponseEntity<viewableEmployee>(retEmp, HttpStatus.OK);
//        return new ResponseEntity<Employee>(employeeServiceImpl.getEmployeeById(empId), HttpStatus.OK);
    }

    @RequestMapping("/getAllEmployees")
    public List<Employee> getAllEmployees() {
        return employeeServiceImpl.getAllEmployees();
    }

    boolean isRankPossible(long employeeRank) {
        return (employeeServiceImpl.getRankCount(employeeRank) < employeeRank);
    }

    Employee addtoHierarchy(Employee employee) throws IOException {
        Random randGen = new Random();
        long superiorRank = employee.getEmployeeRank() - 1;
        while (superiorRank > 0) {
            if (employeeServiceImpl.getRankCount(superiorRank) == 0) {
                superiorRank--;
                continue;
            }
            else {
                break;
            }
        }
        Logger lg = Logger.getInstance();
        long superiorId = 0L;
        lg.log("SuPERIOR Found : " + superiorRank);
        if (superiorRank > 0) {//Superior Available
            List<Long> superiorIds = employeeServiceImpl.getEmployeesByRank(superiorRank);
            for (long empId: superiorIds) {
                lg.log(String.valueOf(empId));
            }
            superiorId = superiorIds.get(randGen.nextInt(superiorIds.size()));
            lg.log("Selected Superior : " + superiorId);
            employee.setReportsTo(superiorId);
            Reportee reportee = new Reportee();
            employee = employeeServiceImpl.saveEmployee(employee);
            lg.log("CHECK2:"+employee);
            reportee.setEmployee_id(superiorId);
            reportee.setReportee(employee.getEmployeeId());
            lg.log("CHECKING FOR REPORTEE ASSIGNMENT" + reportee);
            reporteeServiceImplementation.saveReportee(reportee);
        }
        else {
            lg.log("INSERTING with no superior");
            employee.setReportsTo(0);
            employee = employeeServiceImpl.saveEmployee(employee);
        }
        //find if there are any reportsTo = 0
        lg.log("POST SUPERIOR ASSIGNMENT");
        List<Long> noReportsToIds = employeeServiceImpl.getEmployeesByReportsTo(0, employee.getEmployeeRank());
        lg.log("FOUND REPORTS TO 0" + noReportsToIds);
        long empId = employee.getEmployeeId();
        Reportee repo = new Reportee();
//        Employee repoEmp;
        repo.setEmployee_id(empId);
        for (long repoId: noReportsToIds) {
            lg.log("REPORTS TO MODIFIED : " + repoId + empId);
            //Every (repoId, employee.getEmployeeId()) ni add to reportees
            //Every repoId ki add employee.getEmployeeId() as reportsTo
            repo.setReportee(repoId);
            reporteeServiceImplementation.saveReportee(repo);
//            repoEmp = employeeServiceImpl.getEmployeeById(repoId);
            lg.log("REPORTEE TABLE UPDATED");
//            repoEmp.setReportsTo(empId);
            //ERROR IN updateReportsTo
            lg.log("TEST : " + repoId + "->" + empId);
            try{
            employeeServiceImpl.updateReportsTo(empId, repoId);
            }
            catch (Exception e) {
                lg.log("Caught: " + e);
            }
//            employeeServiceImpl.deleteEmployee(repoId);//Update instead of delete
//            employeeServiceImpl.saveEmployee(repoEmp);
            lg.log("REPORTS TO UPDATED SUCCESSFULLY");
        }
        if (superiorId != 0) {
            //Rebalancing superior reportees of rank > new Employee rank
            List<Long> currPeers = employeeServiceImpl.getEmployeesByReportsTo(superiorId, employee.getEmployeeRank());
            repo.setEmployee_id(employee.getEmployeeId());
            for (long peer: currPeers) {
                repo.setReportee(peer);
                reporteeServiceImplementation.removeReportee(peer);
                reporteeServiceImplementation.saveReportee(repo);

//                repoEmp = employeeServiceImpl.getEmployeeById(peer);
//                repoEmp.setReportsTo(empId);
                employeeServiceImpl.updateReportsTo(empId, peer);
//                employeeServiceImpl.deleteEmployee(peer);//Update instead of delete
//                employeeServiceImpl.saveEmployee(repoEmp);
            }
        }
        return employee;
    }
}

class viewableEmployee {
    private Employee employee;
    private List<Long> reportees;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public List<Long> getReportees() {
        return reportees;
    }

    public void setReportees(List<Long> reportees) {
        this.reportees = reportees;
    }

    @Override
    public String toString() {
        return "viewableEmployee{" +
                "employee=" + employee +
                ", reportees=" + reportees +
                '}';
    }
}