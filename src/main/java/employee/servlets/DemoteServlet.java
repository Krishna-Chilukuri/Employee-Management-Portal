package employee.servlets;

import employee.exception.DemoteException;
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
import static employee.servlets.AddEmployeeServlet.addEmployee;

public class DemoteServlet extends HttpServlet {
    static boolean demoteEmp(Long empId, PrintWriter pw)  throws DemoteException {
        EmployeeFactory ef1 = EmployeeFactory.getInstance();
        Employee oldEmp = ef1.employeeMap.get(empId);
        removeEmployee(empId, pw);
        oldEmp.setEmployeeRank(oldEmp.getEmployeeRank() + 1);
        pw.println("IN DEMOTE: " + oldEmp.getEmployeeRank() + ' ' + oldEmp);
        oldEmp.setReportees(new ArrayList<>());
        oldEmp.setReportsTo(null);
        if(!addEmployee(oldEmp, pw)) {
            oldEmp.setEmployeeRank(oldEmp.getEmployeeRank() - 1);
            addEmployee(oldEmp, pw);
            throw new DemoteException("Demotion from " + oldEmp.getEmployeeRank() + " to " + (oldEmp.getEmployeeRank() + 1) + " is not possible");
        }

        return true;
    }
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        PrintWriter pw = res.getWriter();
        Enumeration<String> e = req.getParameterNames();
        long promId = -1L, numDems = -1L;

        while(e.hasMoreElements()) {
            String pname = e.nextElement();
            if (pname.equals("eid")) {
                promId = Long.parseLong(req.getParameter(pname));
            }
            if (pname.equals("kval")) {
                numDems = Long.parseLong(req.getParameter(pname));
            }
        }

        if (promId == -1 || numDems == -1) {
            pw.println("Promotion not possible");
            return;
        }

        while (numDems > 0) {
            try{
                if(demoteEmp(promId, pw)) numDems--;
            }
            catch (DemoteException de) {
                pw.println("Caught: " + de);
                break;
            }
        }
    }
}
