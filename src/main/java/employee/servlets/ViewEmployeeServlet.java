package employee.servlets;

import employee.factory.DBFactory;
import employee.factory.EmployeeFactory;
import employee.model.Employee;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ViewEmployeeServlet extends HttpServlet {
    static void printEmployees(ResultSet rs1, Connection conn, PrintWriter pw) throws SQLException {
        boolean resStat = false;
        while (rs1.next()) {
            resStat = true;
            long empId = rs1.getLong("employee_id");
            String empName = rs1.getString("employee_name");
            long empRank = rs1.getLong("employee_rank");
            long reportsTo = rs1.getLong("reports_to");
            List<Long> reportees = new ArrayList<>();
            PreparedStatement stmt = conn.prepareStatement("select reportee from reportees where employee_id = ?");
            stmt.setLong(1, empId);
            ResultSet rs2 = stmt.executeQuery();
            while (rs2.next()) {
                reportees.add(rs2.getLong("reportee"));
            }
            pw.println("Employee{" +
                    "employeeId = " + empId +
                    ", employeeName = '" + empName + '\'' +
                    ", employeeRank = " + empRank +
                    ", reportsTo = " + reportsTo +
                    ", reportees = " + reportees +
                    '}');
            rs2.close();
            stmt.close();
        }
        if (!resStat) {
            pw.println("Employee is not present");
        }
    }
    static void getEmployee(long searchId, PrintWriter pw) {
        Connection conn;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            DBFactory dbf = DBFactory.getInstance();
            conn = DriverManager.getConnection(dbf.url, dbf.username, dbf.password);
            PreparedStatement stmt = conn.prepareStatement("select * from employees where employee_id = ?");
            stmt.setLong(1, searchId);
            ResultSet rs1 = stmt.executeQuery();
            printEmployees(rs1, conn, pw);
        }
        catch (SQLException se) { pw.println("Caught SQL Exception : " + se); }
        catch (Exception e) { pw.println("Caught Exception : " + e); }
    }
    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        EmployeeFactory ef1 = EmployeeFactory.getInstance();
        PrintWriter pw = res.getWriter();
        long searchId = 0L;

        Enumeration<String> e = req.getParameterNames();

        while(e.hasMoreElements()) {
            String pname = e.nextElement();
            if (pname.equals("viewId")) {
                searchId = Long.parseLong(req.getParameter(pname));
            }
        }

        pw.println("Search ID Given is : " + searchId);
        if (ef1.employeeMap.containsKey(searchId)) {
            Employee searchEmp = ef1.employeeMap.get(searchId);
            pw.println("ID : " + searchEmp.getEmployeeId());
            pw.println("NAME : " + searchEmp.getEmployeeName());
            pw.println("RANK : " + searchEmp.getEmployeeRank());
            pw.println("Reports to : " + searchEmp.getReportsTo());
            pw.println("Reportees : " + searchEmp.getReportees());
        }
        else {
            pw.println("Employee is not present");
        }

        getEmployee(searchId, pw);
    }
}
