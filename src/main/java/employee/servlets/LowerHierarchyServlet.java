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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;


import static employee.servlets.ViewEmployeeServlet.getEmployee;

public class LowerHierarchyServlet extends HttpServlet {
    static boolean isIdAvailable(HashMap<Long, Employee> empMap, Long empId) {
        return empMap.containsKey(empId);
    }

    static void hierarchyFromEmp(long reqEmp, PrintWriter pw) {
        Queue<Long> empQ = new LinkedList<>();
        long curr;
        List<Employee> currReps;
        empQ.add(reqEmp);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            DBFactory dbf = DBFactory.getInstance();
            Connection conn = DriverManager.getConnection(dbf.url, dbf.username, dbf.password);
            PreparedStatement stmt1 = conn.prepareStatement("select * from employees where employee_id = ?");
            PreparedStatement stmt2 = conn.prepareStatement("select reportee from reportees where employee_id = ?");
            ResultSet rs1, rs2;
            while (empQ.peek() != null) {
                curr = empQ.poll();
                stmt1.setLong(1, curr);
                rs1 = stmt1.executeQuery();
                if (rs1.next()) {
                    long empId = rs1.getLong("employee_id");
                    String empName = rs1.getString("employee_name");
                    long empRank = rs1.getLong("employee_rank");
                    long reportsTo = rs1.getLong("reports_to");
                    List<Long> reportees = new ArrayList<>();
                    //emp is present
                    //find reportees
                    stmt2.setLong(1, curr);
                    rs2 = stmt2.executeQuery();
                    while (rs2.next()) {
                        empQ.add(rs2.getLong("reportee"));
                        reportees.add(rs2.getLong("reportee"));
                    }
                    pw.println("Employee{" +
                            "employeeId = " + empId +
                            ", employeeName = '" + empName + '\'' +
                            ", employeeRank = " + empRank +
                            ", reportsTo = " + reportsTo +
                            ", reportees = " + reportees +
                            '}');
                }
                else {
                    pw.println("Employee not present");
                    return;
                }
            }
        }
        catch (Exception ex) { pw.println("Caught : " + ex); }
    }

    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        Enumeration<String> e = req.getParameterNames();
        EmployeeFactory ef1 = EmployeeFactory.getInstance();
        long empId = -1;
        PrintWriter pw = res.getWriter();

        while(e.hasMoreElements()) {
            String pname = e.nextElement();
            if (pname.equals("eid")) {
                empId = Long.parseLong(req.getParameter(pname));
            }
        }

        pw.println("Lower Hierarchy for : " + empId);
        hierarchyFromEmp(empId, pw);
    }
}
