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

        getEmployees(pw);

        pw.close();
    }
}
