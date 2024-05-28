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

public class UpperHierarchyServlet extends HttpServlet {
    static void hierarchyUptoRank(long targetId, PrintWriter pw) {
        Queue<Long> empQ = new LinkedList<>();
        Queue<Long> rankQ = new LinkedList<>();
        long targetRank, bossRank, curr;
        Connection conn;
        ResultSet rs1, rs2;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            DBFactory dbf = DBFactory.getInstance();
            conn = DriverManager.getConnection(dbf.url, dbf.username, dbf.password);
            PreparedStatement stmt = conn.prepareStatement("select employee_rank from employees where employee_id = ?");
            stmt.setLong(1, targetId);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                pw.println("Employee is not present");
                return;
            }
            targetRank = rs.getLong("employee_rank");

            stmt = conn.prepareStatement("select rankNum from rankCounts WHERE rankCount > 0 ORDER BY rankNum ASC");
            rs = stmt.executeQuery();
            if (!rs.next()) {
                pw.println("Employee is not present");
                return;
            }
            bossRank = rs.getLong("rankNum");
            stmt = conn.prepareStatement("select employee_id from employees where employee_rank = ?");
            stmt.setLong(1, bossRank);
            rs = stmt.executeQuery();
            while(rs.next()) {
                empQ.add(rs.getLong("employee_id"));
                rankQ.add(bossRank);
            }

            stmt = conn.prepareStatement("select employee_id, employee_rank from employees where reports_to = ?");
            PreparedStatement stmt1 = conn.prepareStatement("select * from employees where employee_id = ?");
            PreparedStatement stmt2 = conn.prepareStatement("select reportee from reportees where employee_id = ?");
            while(empQ.peek() != null && rankQ.peek() < targetRank) {
                curr = empQ.poll();
                rankQ.poll();
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
                stmt.setLong(1, curr);
                rs = stmt.executeQuery();
                while (rs.next()) {
                    empQ.add(rs.getLong("employee_id"));
                    rankQ.add(rs.getLong("employee_rank"));
                }
            }

        }
        catch (Exception e) { pw.println("Caught Exception : " + e); }
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

        pw.println("Upper Hierarchy for " + empId);

        hierarchyUptoRank(empId, pw);
    }
}
