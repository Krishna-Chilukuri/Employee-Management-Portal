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

public class LowerHierarchyServlet extends HttpServlet {
    static boolean isIdAvailable(HashMap<Long, Employee> empMap, Long empId) {
        return empMap.containsKey(empId);
    }

    static void hierarchyFromEmp(Employee reqEmp, PrintWriter pw) {
        Queue<Employee> empQ = new LinkedList<>();
        Employee curr;
        List<Employee> currReps;
        empQ.add(reqEmp);

        while (empQ.peek() != null) {
            curr = empQ.poll();
            pw.println("EMP : " + curr.getEmployeeId() + ' ' + curr.getEmployeeName() + ' ' + curr.getEmployeeRank());
            currReps = curr.getReportees();
            if (currReps.isEmpty()) continue;
            empQ.addAll(currReps);
        }
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
        if (!isIdAvailable(ef1.employeeMap, empId)) {
            pw.println("ID is not available");
            return;
        }

        Employee reqEmp = ef1.employeeMap.get(empId);

        hierarchyFromEmp(reqEmp, pw);
    }
}
