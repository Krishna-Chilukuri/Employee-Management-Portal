package employee.servlets;

import employee.factory.DBFactory;
import employee.factory.EmployeeFactory;
import employee.model.Employee;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    }
    static synchronized boolean addEmployee(Employee newEmp, PrintWriter pw) {
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
        }
        catch (SQLException se) { pw.println("Caught SQL Exception : " + se); }
        catch (Exception e) { pw.println("Caught Exception : " + e); }
        return true;
    }

    static String readCookie(HttpServletRequest req, String key) {
        return Arrays.stream(req.getCookies())
                .filter(cookie -> key.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findAny()
                .map(Object::toString)
                .orElse("");
    }

    static boolean checkPrivilege(String reqUserId, long newEmpRank, PrintWriter pw, HttpServletResponse res) {
        Connection conn;
        DBFactory dbf = DBFactory.getInstance();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(dbf.url, dbf.username, dbf.password);
            PreparedStatement stmt = conn.prepareStatement("select privilege from login where username = ?");
            stmt.setString(1, reqUserId);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                pw.println("No user of your ID found");
                Cookie userDetails = new Cookie("user", "");
                userDetails.setMaxAge(0);
                res.addCookie(userDetails);
                return false;
            }
            String privilege = rs.getString("privilege");
            if (privilege.equals("owner") || privilege.equals("admin")) {
                return true;
            }
            else if (privilege.equals("guest")) {
                pw.println("Guests are permitted only to view Users but not to manipulate the hierarchies");
                return false;
            }
            else if (privilege.equals("priv_user")){
                stmt = conn.prepareStatement("select employee_rank from employees where employee_id = ?");
                stmt.setLong(1, Long.parseLong(reqUserId));
                rs = stmt.executeQuery();
                if (!rs.next()) {
                    return false;
                }
                long reqUserRank = rs.getLong("employee_rank");
                if (reqUserRank < newEmpRank) {
                    return true;
                }
            }
            else {
                pw.println("User privilege type doesn't exist");
                return false;
            }
        }
        catch (Exception e) {
            pw.println("Caught Exception : " + e);
//            e.printStackTrace();
        }
        return false;
    }

    static void addUser(long empId, String userPwd) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            DBFactory dbf = DBFactory.getInstance();
            Connection conn = DriverManager.getConnection(dbf.url, dbf.username, dbf.password);
            PreparedStatement stmt = conn.prepareStatement("insert into login values(?, ?, ?)");
            stmt.setString(1, String.valueOf(empId));
            stmt.setString(2, userPwd);
            stmt.setString(3, "priv_user");
            stmt.executeUpdate();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
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

        String reqUserId = readCookie((HttpServletRequest) req,"user");
        pw.println("reqUserId is : " + reqUserId);
        if (!checkPrivilege(reqUserId, newEmp.getEmployeeRank(), pw, (HttpServletResponse) res)) {
            pw.println("You can't insert this employee");
            return;
        }

        if (!addEmployee(newEmp, pw)) {
            return;
        }

        addUser(newEmp.getEmployeeId(), newEmp.getEmployeeName());

        pw.println(newEmp);

        pw.close();

    }
}
