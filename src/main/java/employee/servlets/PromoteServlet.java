package employee.servlets;

import employee.exception.DemoteException;
import employee.exception.PromoteException;
import employee.factory.DBFactory;
import employee.factory.EmployeeFactory;
import employee.model.Employee;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;

import static employee.servlets.AddEmployeeServlet.*;
import static employee.servlets.RemoveEmployeeServlet.removeEmployee;
import static employee.servlets.RemoveEmployeeServlet.handleReportees;


public class PromoteServlet extends HttpServlet {
    static boolean promoteEmp(Long empId, PrintWriter pw) throws PromoteException {
        Connection conn;
        DBFactory dbf = DBFactory.getInstance();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(dbf.url, dbf.username, dbf.password);
            PreparedStatement stmt = conn.prepareStatement("select * from employees where employee_id = ?");
            stmt.setLong(1, empId);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            long empRank = rs.getLong("employee_rank");
            String empName = rs.getString("employee_name");
            if (empRank == 1) throw new PromoteException("1 is the highest rank possible");
            if (!removeEmployee(empId, pw)) {
                pw.println("Promotion not possible");
            }
            empRank--;
            Employee newEmp = new Employee();
            newEmp.setEmployeeId(empId);
            newEmp.setEmployeeRank(empRank);
            newEmp.setEmployeeName(empName);
            if (!addEmployee(newEmp, pw)) {
                newEmp.setEmployeeRank(newEmp.getEmployeeRank() + 1);
                addEmployee(newEmp, pw);
                throw new PromoteException("Promotion from " + newEmp.getEmployeeRank() + " to " + (newEmp.getEmployeeRank() - 1) + " is not possible");
            }
        }
        catch (Exception e) { pw.println("Caught Exception : " + e); }

        return true;
    }
    static long getEmployeeRank(long empId) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            DBFactory dbf = DBFactory.getInstance();
            Connection conn = DriverManager.getConnection(dbf.url, dbf.username, dbf.password);
            PreparedStatement stmt = conn.prepareStatement("select employee_rank from employees where employee_id = ?");
            stmt.setLong(1, empId);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                return -1;
            }
            return rs.getLong("employee_rank");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        PrintWriter pw = res.getWriter();
        Enumeration<String> e = req.getParameterNames();
        long promId = -1L, numProms = -1L;

        while(e.hasMoreElements()) {
            String pname = e.nextElement();
            if (pname.equals("eid")) {
                promId = Long.parseLong(req.getParameter(pname));
            }
            if (pname.equals("kval")) {
                numProms = Long.parseLong(req.getParameter(pname));
            }
        }

        if (promId == -1 || numProms == -1) {
            pw.println("Promotion not possible");
            return;
        }
        long newRank = getEmployeeRank(promId);
        if (newRank == -1) {
            pw.println("Employee to promote is not available");
            return;
        }
        newRank = newRank - numProms;

        String reqUserId = readCookie((HttpServletRequest) req, "user");
        pw.println("Req User ID : " + reqUserId);

        if (!checkPrivilege(reqUserId, newRank, pw, (HttpServletResponse) res)) {
            pw.println("You can't promote " + promId);
            return;
        }

        while (numProms > 0) {
            try{
                if(promoteEmp(promId, pw)) {
                    numProms--;
                }
            }
            catch (PromoteException pe) {
                pw.println("Caught: " + pe);
                break;
            }
        }
    }
}
