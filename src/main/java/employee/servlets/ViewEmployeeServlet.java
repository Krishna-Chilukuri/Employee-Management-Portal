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

public class ViewEmployeeServlet extends HttpServlet {
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
    }
}
