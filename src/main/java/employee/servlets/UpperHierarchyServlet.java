package employee.servlets;

import employee.factory.EmployeeFactory;
import employee.model.Employee;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;

public class UpperHierarchyServlet extends HttpServlet {
    static boolean isIdAvailable(HashMap<Long, Employee> empMap, Long empId) {
        return empMap.containsKey(empId);
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
        if (!isIdAvailable(ef1.employeeMap, empId)) {
            pw.println("ID is not available");
            return;
        }
        
    }
}
