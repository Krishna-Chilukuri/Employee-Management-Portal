package employee.servlets;

import employee.factory.DBFactory;
import employee.factory.EmployeeFactory;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static employee.servlets.ViewEmployeeServlet.printEmployees;

public class ViewAllEmployees extends HttpServlet {
    static void getEmployees(PrintWriter pw) {
        Connection conn;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            DBFactory dbf = DBFactory.getInstance();
            conn = DriverManager.getConnection(dbf.url, dbf.username, dbf.password);
            PreparedStatement stmt = conn.prepareStatement("select * from employees");
            ResultSet rs1 = stmt.executeQuery();
            printEmployees(rs1, conn, pw);
            rs1.close();
            stmt.close();
            conn.close();
        }
        catch (SQLException se) {
            pw.println("Caught SQL Exception : " + se);
        }
        catch (Exception e) {
            pw.println("Caught Exception : " + e);
        }
    }
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        EmployeeFactory ef1 = EmployeeFactory.getInstance();
        PrintWriter pw = res.getWriter();

//        ef1.employeeMap.forEach((key, val) ->
//                pw.println(key + " => " + val.getEmployeeId() + " " + val.getEmployeeName() + " " + val.getEmployeeRank() + " " + val.getReportsTo() + " " + val.getReportees() + " " +val));
//
//        ef1.rankMap.forEach((key, val) ->
//                pw.println(key + " rank => " + val));

        getEmployees(pw);

//        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
//            DBFactory dbf = DBFactory.getInstance();
//            conn = DriverManager.getConnection(dbf.url, dbf.username, dbf.password);
//
//            Statement stmt = conn.createStatement();
//            ResultSet rs1;
////            ResultSet rs2;
//            rs1 = stmt.executeQuery("select * from employees");
//            while(rs1.next()) {
////                Employee emp = new Employee();
//                long empId = rs1.getLong("employee_id");
//                pw.println("EMP ID : " + empId);
//                Statement stmt2 = conn.createStatement();
//                ResultSet rs2 = stmt2.executeQuery("select reportee from reportees where employee_id = "+empId);
//                String empName = rs1.getString("employee_name");
//                long empRank = rs1.getLong("employee_rank");
//                long reportsTo = rs1.getLong("reports_to");
//                reportees.clear();
//                while(rs2.next()) {
//                    reportees.add(rs2.getLong("reportee"));
//                }
//                pw.println("Employee{" +
//                        "employeeId = " + empId +
//                        ", employeeName = '" + empName + '\'' +
//                        ", employeeRank = " + empRank +
//                        ", reportsTo = " + reportsTo +
//                        ", reportees = " + reportees +
//                        '}');
//                rs2.close();
//                stmt2.close();
//            }
//            rs1.close();
//            stmt.close();
//            conn.close();
//        }
//        catch (SQLException se) {
//            pw.println("SQL Exception : " + se);
//        }
//        catch (Exception e) {
//            pw.println("Exception : " + e);
//        }

        pw.close();
    }
}
