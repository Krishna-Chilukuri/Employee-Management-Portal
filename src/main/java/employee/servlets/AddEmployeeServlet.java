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
        //curr superior rank dorikindi
        //get all superiors of currSuperior Rank
        if (currSuperiorRank > 0) {
            List<Long> superiorIds = new ArrayList<>();
            stmt = conn.prepareStatement("select employee_id from employees where employee_rank")
        }
//        Long currSuperiorRank = testEmp.getEmployeeRank() - 1;
//        while (!rankHashMap.containsKey(currSuperiorRank) || (rankHashMap.containsKey(currSuperiorRank) && rankHashMap.get(currSuperiorRank).isEmpty())) {
//            if (currSuperiorRank == 0) break;
//            currSuperiorRank--;
//        }
//        testEmp.setReportees(new ArrayList<>());
//
//        if (currSuperiorRank > 0) {
//            Long superior = rankHashMap.get(currSuperiorRank).get(randGen.nextInt(rankHashMap.get(currSuperiorRank).size()));
//            employeeHashMap.get(superior).addReportee(testEmp);
//            testEmp.setReportsTo(employeeHashMap.get(superior));
//        }
//
//        employeeHashMap.put(testEmp.getEmployeeId(), testEmp);
//        rankHashMap.get(testEmp.getEmployeeRank()).add(testEmp.getEmployeeId());
        return true;
    }

    static void rebalanceHierarchy(HashMap<Long, Employee> employeeHashMap, Employee newEmp) {
        Employee currBoss = newEmp.getReportsTo();
        if (currBoss == null) return;
        long newEmpRank = newEmp.getEmployeeRank();

//        List<Employee> newEmpReportees = new ArrayList<Employee>();
        List<Employee> currBossReportees = currBoss.getReportees();
        List<Long> changeIDs = new ArrayList<>();

        for(Employee currReportee : currBossReportees) {
            if (currReportee.getEmployeeRank() > newEmpRank) {
                changeIDs.add(currReportee.getEmployeeId());
            }
        }

        for (Long changeID : changeIDs) {
            Employee currRep = employeeHashMap.get(changeID);
            currBoss.removeReportee(currRep);
            currRep.setReportsTo(newEmp);
            newEmp.addReportee(currRep);
        }
//
//        for (Employee currReportee : currBossReportees) {
//            if (currReportee.getEmployeeRank() > newEmpRank) {
//                currBoss.removeReportee(currReportee);
//                currReportee.setReportsTo(newEmp);
//                newEmp.addReportee(currReportee);
//            }
//            newEmp.setReportees(newEmpReportees);
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
            if (!addToHierarchy(conn, newEmp, pw)) {
                pw.println("Hierarchy can't be set");
                return false;
            }
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
