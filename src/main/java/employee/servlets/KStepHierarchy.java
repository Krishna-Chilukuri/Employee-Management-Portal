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
import java.util.*;

public class KStepHierarchy extends HttpServlet {
    static boolean isIdAvailable(HashMap<Long, Employee> empMap, Long empId) {
        return empMap.containsKey(empId);
    }
    static List<Long> kStepHierarchy(long empId, long k, PrintWriter pw) {
        boolean breakStat = false;
        long locK = k, currK;
        List<Long> retkStep = new ArrayList<>();
        long curr = empId;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            DBFactory dbf = DBFactory.getInstance();
            Connection conn = DriverManager.getConnection(dbf.url, dbf.username, dbf.password);
            PreparedStatement stmt = conn.prepareStatement("select reports_to from employees where employee_id = ?");
            ResultSet rs;
            while (curr > 0 && locK > 0) {
                stmt.setLong(1, curr);
                rs = stmt.executeQuery();
                if (!rs.next()) {
                    pw.println("No more upper employee available");
                    breakStat = true;
                    break;
                }
                curr = rs.getLong("reports_to");
                locK--;
            }
            if (!breakStat) {
                retkStep.add(curr);
            }
            locK = k;
            Queue<Long> kStepDownQ = new LinkedList<>();
            Queue<Long> kStepDownK = new LinkedList<>();
            kStepDownQ.add(empId);
            kStepDownK.add(k);
            stmt = conn.prepareStatement("select reportee from reportees where employee_id = ?");
            while (kStepDownQ.peek() != null) {
                curr = kStepDownQ.poll();
                currK = kStepDownK.poll();
                if (currK > 0){
                    currK--;
                    stmt.setLong(1, curr);
                    rs = stmt.executeQuery();
                    while (rs.next()) {
                        kStepDownQ.add(rs.getLong("reportee"));
                        kStepDownK.add(currK);
                    }
                } else if (currK == 0) {
                    retkStep.add(curr);
                }
            }
        }
        catch (Exception e) {
            pw.println("Caught Exception : " + e);
        }
        return retkStep;
    }

    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        Enumeration<String> e = req.getParameterNames();
        EmployeeFactory ef1 = EmployeeFactory.getInstance();
        long empId = -1, kval = -1;
        PrintWriter pw = res.getWriter();

        while(e.hasMoreElements()) {
            String pname = e.nextElement();
            if (pname.equals("eid")) {
                empId = Long.parseLong(req.getParameter(pname));
            }
            if (pname.equals("kval")) {
                kval = Long.parseLong(req.getParameter(pname));
            }
        }

        pw.println("K Step Hierarchy for " + empId);
//        if (!isIdAvailable(ef1.employeeMap, empId)) {
//            pw.println("ID is not available");
//            return;
//        }
        if (kval < 0) {
            pw.println("Look into urself");
            return;
        }

        Employee centerEmp = ef1.employeeMap.get(empId);
//        List<String> retList = kStepHierarchy(centerEmp, kval, pw);
        List<Long> retList = kStepHierarchy(empId, kval, pw);

        for (Long retEmpId: retList) {
            pw.println(retEmpId);
//            pw.println("EMP : " + emp.getEmployeeId() + " " + emp.getEmployeeName() + ' ' + emp.getEmployeeRank());
        }
    }
}
