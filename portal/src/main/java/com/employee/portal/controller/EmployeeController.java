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
import org.apache.juli.logging.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
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
        lg.log("HERE : " + employeeServiceImpl.getEmployeeById(empId));
        if (employeeServiceImpl.getEmployeeById(empId).getEmployeeName() == null) {
            lg.log("EMPloyee not available to delete");
            return;
        }
        handleReportees(empId);
        lg.log("REPORTEES HANDLED");
        try{
        employeeServiceImpl.deleteEmployee(empId);
        lg.log("Employee deleted");

        loginServiceImplementation.removeLogin(String.valueOf(empId));
        }
        catch (Exception e) {
            lg.log("CAUGHT : " + e);
        }
//        return new ResponseEntity<Employee>(new Employee(), HttpStatus.OK);
    }

    @RequestMapping("/promote")
    public ResponseEntity<String> promoteEmployee(@RequestParam(name = "empId") long empId, @RequestParam(name = "numProms") long numProms) throws IOException {
        Logger lg = Logger.getInstance();
        lg.log("Promote Request received for :" + empId + " for " + numProms +" promotions");
        Employee employee = employeeServiceImpl.getEmployeeById(empId);
        if (employee.getEmployeeName() == null) {
            lg.log("Employee is not present to promote");
            return new ResponseEntity<String>("Employee not Found", HttpStatus.NOT_FOUND);
        }
        if (employee.getEmployeeRank() - numProms <= 0) {
            lg.log("These number of promotions is not possible");
            return new ResponseEntity<>("These number of promotions is not possible", HttpStatus.BAD_REQUEST);
        }
        promoteProcess(employee, numProms);//Take return and send response based on it. RAnk max cap  or promotion from 1
        return new ResponseEntity<String>("Promotion Done", HttpStatus.OK);
    }

    @RequestMapping("/demote")
    public ResponseEntity<String> demoteEmployee(@RequestParam(name = "empId") long empId, @RequestParam(name = "numDems") long numDems) throws IOException {
        Logger lg = Logger.getInstance();
        lg.log("Demote Request received for :" + empId + " for " + numDems + " demotions");
        Employee employee = employeeServiceImpl.getEmployeeById(empId);
        if (employee.getEmployeeName() == null) {
            lg.log("Employee is not present to demote");
            return new ResponseEntity<String>("Employee not Found", HttpStatus.NOT_FOUND);
        }
        demoteProcess(employee, numDems);
        return new ResponseEntity<String>("Demotion Done", HttpStatus.OK);
    }

    void promoteProcess(Employee employee, long numProms) throws IOException {
        Logger lg = Logger.getInstance();
        while (numProms > 0) {
            if (employee.getEmployeeRank() == 1) {//Redundant check (checked already at promoteEmployee())
                lg.log("Promotion from 1 is not possible");
                break;
            }
            long nextRank = employee.getEmployeeRank() - 1;
            long nextLvlCount = employeeServiceImpl.getRankCount(nextRank);
            if (nextLvlCount == nextRank) {
                lg.log("RANK MAX CAP REACHED");
                break;
            }
            handleReportees(employee.getEmployeeId());
            employee.setEmployeeRank(nextRank);
            employee = addtoHierarchy(employee);
            numProms--;
            lg.log("Promoted " + employee.getEmployeeId() + " to " + employee.getEmployeeRank());
        }
    }

    void demoteProcess(Employee employee, long numDems) throws IOException {
        Logger lg = Logger.getInstance();
        while (numDems > 0) {
            long nextRank = employee.getEmployeeRank() + 1;
            long nextLvlCount = employeeServiceImpl.getRankCount(nextRank);
            if (nextLvlCount == nextRank) {
                lg.log("RANK MAX CAP REACHED");
                break;
            }
            handleReportees(employee.getEmployeeId());
            employee.setEmployeeRank(nextRank);
            employeeServiceImpl.updateEmployee(employee, employee.getEmployeeId());
            lg.log("Emp Of Rank : " + (nextRank - 1) + " are : " + employeeServiceImpl.getEmployeesByRank(nextRank - 1) + "curr emp : " + employee);
            employee = addtoHierarchy(employee);
            numDems--;
            lg.log("Demoted " + employee.getEmployeeId() + " to " + employee.getEmployeeRank());
        }
    }

    public void handleReportees(long empId) throws IOException {
        Random randGen = new Random();
        Logger lg = Logger.getInstance();
        List<Long> reporteesIds = employeeServiceImpl.getReporteesOfId(empId);
        if (reporteesIds.isEmpty()) {
            lg.log("No reportees to handle");
            return;
        }
        lg.log("Reportees to handle : "+ reporteesIds );
        long newReportsTo = 0L;
        long remRank = employeeServiceImpl.getEmployeeById(empId).getEmployeeRank();
        List<Long> colleagueIds = employeeServiceImpl.getEmployeesByRank(remRank);
        colleagueIds.remove(empId);
        lg.log("Colleagues : " + colleagueIds);
        if (colleagueIds.isEmpty()) {
            //find superior
            long superiorId = employeeServiceImpl.getEmployeeById(empId).getReportsTo();
            if (superiorId == 0) {
                //no superiors: Promote the highest reportee and use as new Boss
                newReportsTo = reporteesIds.remove(0);
                lg.log("new Boss : " + newReportsTo);
                reporteeServiceImplementation.removeReportee(newReportsTo);
                Employee emp = employeeServiceImpl.getEmployeeById(newReportsTo);
                emp.setReportsTo(0);
                if (emp.getEmployeeRank() >= 2) emp.setEmployeeRank(emp.getEmployeeRank() - 1);
                lg.log("NEW BOSS SELECTED FROM REPORTEE");
            }
            else {
                reporteeServiceImplementation.removeReportee(empId);
                newReportsTo = superiorId;
            }
        }
        else {
            newReportsTo = colleagueIds.get(randGen.nextInt(colleagueIds.size()));
        }
        Reportee repo = new Reportee();
        repo.setEmployee_id(newReportsTo);
        for (long repoId: reporteesIds) {
            employeeServiceImpl.updateReportsTo(newReportsTo, repoId);
            reporteeServiceImplementation.removeReportee(repoId);
            repo.setReportee(repoId);
            reporteeServiceImplementation.saveReportee(repo);
        }
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
        Logger lg = Logger.getInstance();
        long superiorRank = employee.getEmployeeRank() - 1;
        lg.log("SUPERIOR RANK : " + superiorRank);
        while (superiorRank > 0) {
            if (employeeServiceImpl.getRankCount(superiorRank) == 0) {
                superiorRank--;
                continue;
            }
            else {
                break;
            }
        }
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