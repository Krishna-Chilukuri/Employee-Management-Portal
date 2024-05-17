package employee.servlets;

import employee.factory.EmployeeFactory;
import employee.model.Employee;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class KStepHierarchy extends HttpServlet {
    static boolean isIdAvailable(HashMap<Long, Employee> empMap, Long empId) {
        return empMap.containsKey(empId);
    }
    static List<Employee> kStepHierarchy(Employee centerEmp, Long k, PrintWriter pw) {
        boolean breakStat = false;
        Long locK = k, currK;
        Employee curr;
        List<Employee> retkStep = new ArrayList<>();
        curr = centerEmp;

        while(curr.getReportsTo() != null) {
            if (locK == 0) {
                retkStep.add(curr);
                breakStat = true;
                break;
            }
            curr = curr.getReportsTo();
            locK--;
        }
        if (!breakStat) {
            pw.println("No Superiors present " + k + " steps from " + centerEmp.getEmployeeId());
        }
        breakStat = false;
        locK = k;
        Queue<Employee> kStepDownQ = new LinkedList<>();
        Queue<Long> kStepDownK = new LinkedList<>();
        kStepDownQ.add(centerEmp);
        kStepDownK.add(locK);
        while (kStepDownQ.peek() != null && locK > 0) {
            curr = kStepDownQ.poll();
            currK = kStepDownK.poll();
            if (currK > 0) {
                currK--;
                for (Employee emp: curr.getReportees()) {
                    kStepDownQ.add(emp);
                    kStepDownK.add(currK);
                }
            } else if (currK == 0) {
                retkStep.add(curr);
            }
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

        pw.println("Upper Hierarchy for " + empId);
        if (!isIdAvailable(ef1.employeeMap, empId)) {
            pw.println("ID is not available");
            return;
        }
        if (kval < 0) {
            pw.println("Look into urself");
            return;
        }

        Employee centerEmp = ef1.employeeMap.get(empId);
        List<Employee> retList = kStepHierarchy(centerEmp, kval, pw);

        for (Employee emp: retList) {
            pw.println("EMP : " + emp.getEmployeeId() + " " + emp.getEmployeeName() + ' ' + emp.getEmployeeRank());
        }
    }
}
