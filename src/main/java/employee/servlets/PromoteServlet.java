package employee.servlets;

import employee.exception.PromoteException;
import employee.factory.EmployeeFactory;
import employee.model.Employee;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;

import static employee.servlets.RemoveEmployeeServlet.removeEmployee;
import static employee.servlets.RemoveEmployeeServlet.handleReportees;
import static employee.servlets.AddEmployeeServlet.addEmployee;


public class PromoteServlet extends HttpServlet {
    static boolean promoteEmp(Long empId, PrintWriter pw) throws PromoteException {
        EmployeeFactory ef1 = EmployeeFactory.getInstance();
        Employee oldEmp = ef1.employeeMap.get(empId);
        removeEmployee(empId, pw);
        oldEmp.setEmployeeRank(oldEmp.getEmployeeRank() - 1);
        pw.println("IN PROM: " + oldEmp.getEmployeeRank() + ' ' + oldEmp.getEmployeeRank() + oldEmp + ' ' + oldEmp);
        oldEmp.setReportees(new ArrayList<>());
        oldEmp.setReportsTo(null);
        if (!addEmployee(oldEmp, pw)) {
            oldEmp.setEmployeeRank(oldEmp.getEmployeeRank() + 1);
            addEmployee(oldEmp, pw);
            throw new PromoteException("Promotion from " + oldEmp.getEmployeeRank() + " to " + (oldEmp.getEmployeeRank() - 1) + " is not possible");
        }

        return true;
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
