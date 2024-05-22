package employee.servlets;

import employee.factory.DBFactory;
import employee.factory.EmployeeFactory;
import employee.model.Employee;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.*;

import static employee.servlets.ViewAllEmployees.getEmployees;

public class AddEmployeeServlet extends HttpServlet {

    static boolean isDuplicateEmployee(Connection conn, long empId) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("select * from employees where employee_id = ?");
        stmt.setLong(1, empId);
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }

    static boolean isRankPossible(Connection conn, long empRank) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("select count(*) as ct from employees where employee_rank = ?");
        stmt.setLong(1, empRank);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            if (rs.getLong("ct") < empRank) return true;
        }
        return false;
    }

    // Add to hierarchy
    // find superior upto rank 1
    // if no superior is available, this emp is boss
    // if superior exists, add testEmp to the reportee list of superior
    static synchronized boolean addToHierarchy(Connection conn, Employee testEmp, PrintWriter pw) throws SQLException {
        Random randGen = new Random();
        long currSuperiorRank = testEmp.getEmployeeRank() - 1;
        PreparedStatement stmt = conn.prepareStatement("select count(*) as ct from employees where employee_rank = ?");
        while (currSuperiorRank > 0) {
            stmt.setLong(1, currSuperiorRank);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                if (rs.getLong("ct") == 0) currSuperiorRank--;
                else break;
            }
        }
        pw.println("IN ADD TO HIERARCHY Superior: " + currSuperiorRank);
        if (currSuperiorRank > 0) {
            List<Long> superiorIds = new ArrayList<>();
            stmt = conn.prepareStatement("select employee_id from employees where employee_rank = ?");
            stmt.setLong(1, currSuperiorRank);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                superiorIds.add(rs.getLong("employee_id"));
            }
            pw.println("Superiors of RANK : " + currSuperiorRank + " => " + superiorIds);
            long superiorId = superiorIds.get(randGen.nextInt(superiorIds.size()));
            stmt = conn.prepareStatement("insert into reportees values(?, ?)");
            stmt.setLong(1, superiorId);
            stmt.setLong(2, testEmp.getEmployeeId());
            int response = stmt.executeUpdate();
            pw.println("After Update : " + response);

            stmt = conn.prepareStatement("insert into employees values(?, ?, ?, ?)");
            stmt.setLong(1, testEmp.getEmployeeId());
            stmt.setString(2, testEmp.getEmployeeName());
            stmt.setLong(3, testEmp.getEmployeeRank());
            stmt.setLong(4, superiorId);
            response = stmt.executeUpdate();
            pw.println("After Insertion: " + response);
            Employee superior = new Employee();
            superior.setEmployeeId(superiorId);
            testEmp.setReportsTo(superior);
        }
        else {
            stmt = conn.prepareStatement("insert into employees values(?, ?, ?, null)");
            stmt.setLong(1, testEmp.getEmployeeId());
            stmt.setString(2, testEmp.getEmployeeName());
            stmt.setLong(3, testEmp.getEmployeeRank());
            int response = stmt.executeUpdate();
            pw.println("After Insertion if no superior : " + response);
        }
        stmt = conn.prepareStatement("select employee_id, employee_rank from employees where reports_to is NULL");
        ResultSet rs = stmt.executeQuery();
        pw.println("After looking for reports to NULL");
        while (rs.next()) {
            pw.println("MANAGING NEW EMP REPORTEES");
            if (rs.getLong("employee_rank") > testEmp.getEmployeeRank()) {
                pw.println("MANAGING NEW EMP REPORTEES INTO IF");
                stmt = conn.prepareStatement("update employees set reports_to = ? where employee_id = ?");
                stmt.setLong(1, testEmp.getEmployeeId());
                stmt.setLong(2, rs.getLong("employee_id"));
                if (stmt.executeUpdate() == 1) {
                    pw.println("Reports to for rank " + rs.getLong("employee_rank") + " is updated");
                }
                else {
                    throw new SQLException("Error while updation of reportsTo in employees table");
                }
                //add employee_id from query as reportee to testemp
                stmt = conn.prepareStatement("insert into reportees values (?, ?)");
                stmt.setLong(1, testEmp.getEmployeeId());
                stmt.setLong(2, rs.getLong("employee_id"));
                if (stmt.executeUpdate() == 1) {
                    pw.println("new employee is set as superior for " + rs.getLong("employee_id"));
                }
                else {
                    throw new SQLException("Error while insertion into reportees table");
                }
            }
        }
        return true;
    }

    static void rebalanceHierarchy(Connection conn, Employee newEmp, PrintWriter pw) throws SQLException {
        Employee currBoss = newEmp.getReportsTo();
        if (currBoss == null) {
            pw.println("IN REBALANCE : currBoss is null");
            return;
        }
        long newEmpRank = newEmp.getEmployeeRank();
        List<Long> currBossReportees = new ArrayList<>();
        PreparedStatement stmt = conn.prepareStatement("select reportee, employee_rank from employees, reportees where employees.employee_id = reportee and reportees.employee_id = ?");
//        stmt.setLong(1, newEmp.getEmployeeId());
        stmt.setLong(1, currBoss.getEmployeeId());
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            long reporteeId = rs.getLong("reportee");
            long reporteeRank = rs.getLong("employee_rank");
            pw.println("IN WHILE : " + reporteeId + ' ' +reporteeRank);
            if (reporteeRank > newEmp.getEmployeeRank()) {
                pw.println("IN ADD:::::::::::: " + reporteeRank + ' ' + newEmp.getEmployeeRank());
                pw.println("IN IF IN WHILE");
                //change reportee to newEmp
                stmt = conn.prepareStatement("update reportees set employee_id = ? where reportee = ?");
                stmt.setLong(1, newEmp.getEmployeeId());
                stmt.setLong(2, reporteeId);
                if (stmt.executeUpdate() == 1) {
                    pw.println("Reportee Updated");
                }
                else {
                    throw new SQLException("Error while updating superior for " + reporteeId);
                }
                pw.println("IN BETWEEN UPDATIONS");
                stmt = conn.prepareStatement("update employees set reports_to = ? where employee_id = ?");
                stmt.setLong(1, newEmp.getEmployeeId());
                stmt.setLong(2, reporteeId);
                if (stmt.executeUpdate() == 1) {
                    pw.println("Reports to in Employees updated");
                }
                else{
                    throw new SQLException("Error while updating reports to in employees for "+ reporteeId);
                }
            }
        }

//        List<Employee> currBossReportees = currBoss.getReportees();
//        List<Long> changeIDs = new ArrayList<>();
//
//        for(Employee currReportee : currBossReportees) {
//            if (currReportee.getEmployeeRank() > newEmpRank) {
//                changeIDs.add(currReportee.getEmployeeId());
//            }
//        }
//
//        for (Long changeID : changeIDs) {
//            Employee currRep = employeeHashMap.get(changeID);
//            currBoss.removeReportee(currRep);
//            currRep.setReportsTo(newEmp);
//            newEmp.addReportee(currRep);
//        }
    }
    static boolean addEmployee(Employee newEmp, PrintWriter pw) {
        EmployeeFactory ef1 = EmployeeFactory.getInstance();
        Connection conn;
        DBFactory dbf = DBFactory.getInstance();

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(dbf.url, dbf.username, dbf.password);
            if (isDuplicateEmployee(conn, newEmp.getEmployeeId())) {
                pw.println("Duplicate Employee");
                return false;
            }
            if (!isRankPossible(conn, newEmp.getEmployeeRank())) {
                pw.println("Insertion in rank is not possible");
                return false;
            }
            pw.println("BEFORE ADDING " + newEmp.getEmployeeId() + ' ' + newEmp.getEmployeeRank() + " to hierarchy");
            getEmployees(pw);
            if (!addToHierarchy(conn, newEmp, pw)) {
                pw.println("Hierarchy can't be set");
                return false;
            }
            pw.println("After adding to hie");
            getEmployees(pw);
            rebalanceHierarchy(conn, newEmp, pw);
            pw.println("After rebalancing");
            getEmployees(pw);
            PreparedStatement stmt = conn.prepareStatement("insert into rankCounts (rankNum, rankCount) VALUES (?, 1) on duplicate key update rankCount = rankCount + 1");
            stmt.setLong(1, newEmp.getEmployeeRank());
            stmt.executeUpdate();
//            if (stmt.executeUpdate() == 1) {
//                pw.println("Rank Count table updated");
//            }
//            else {
//                throw new SQLException("Error while updating rank count map");
//            }
        }
        catch (SQLException se) { pw.println("Caught SQL Exception : " + se); }
        catch (Exception e) { pw.println("Caught Exception : " + e); }
//        if (isDuplicateEmployee(ef1.employeeMap, newEmp)) {
//            pw.println("Duplicate Employee, Insertion not Possible");
//            return false;
//        }
//        //does the rank provided align with the hierarchy
//        if (isRankNotPossible(ef1.rankMap, newEmp)) {
//            pw.println("Insertion in rank is not possible");
//            pw.println("HERE : " + ef1.rankMap.get(newEmp.getEmployeeRank()).size() + ' ' + newEmp.getEmployeeRank());
//            return false;
//        }
//
//        if (!addToHierarchy(ef1.employeeMap, ef1.rankMap, newEmp)) {
//            pw.println("Hierarchy can't be set");
//            return false;
//        }
//        rebalanceHierarchy(ef1.employeeMap, newEmp);
//        pw.println("RANK CURR LEN : " + ef1.rankMap.get(newEmp.getEmployeeRank()).size());
//        pw.println("SIZE : " + ef1.employeeMap.get(newEmp.getEmployeeId()).getReportees().size());
        return true;
    }

    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
//        EmployeeFactory ef1 = EmployeeFactory.getInstance();
        PrintWriter pw = res.getWriter();
        Employee newEmp = new Employee();
        long higherRank;
        boolean insertionStat = false;

        Enumeration<String> e = req.getParameterNames();
        while(e.hasMoreElements()) {
            String pname = e.nextElement();
            if (pname.equals("eid")) {
                newEmp.setEmployeeId(Long.parseLong(req.getParameter(pname)));
            }
            else if(pname.equals("ename")) {
                newEmp.setEmployeeName(req.getParameter(pname));
            }

            else if(pname.equals("erank")) {
                newEmp.setEmployeeRank(Long.parseLong(req.getParameter(pname)));
            }
        }

        if (!addEmployee(newEmp, pw)) {
            return;
        }

        //is Employee being inserted a duplicate
        pw.println(newEmp);

//        pw.println("Employee Added");
//        pw.println("ID : " + newEmp.getEmployeeId());
//        pw.println("Name : " + newEmp.getEmployeeName());
//        pw.println("Rank : " + newEmp.getEmployeeRank());//toString()

//        if (!addToHierarchy(ef1.employeeMap, ef1.rankMap, newEmp)) {
//            pw.println("Hierarchy can't be set");
//        }
//        else {
//        }


        pw.close();

    }
}
