package employee.servlets;

import employee.factory.EmployeeFactory;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

public class RemoveEmployeeServlet extends HttpServlet {
    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        EmployeeFactory ef1 = EmployeeFactory.getInstance();

        Enumeration<String> e = req.getParameterNames();

        long remId = -1;
        PrintWriter pw = res.getWriter();

        while(e.hasMoreElements()) {
            String pname = e.nextElement();
            if (pname.equals("remid")) {
                remId = Long.parseLong(req.getParameter(pname));
            }
        }

        if (ef1.employeeMap.containsKey(remId)) {
            pw.println("EMP IS PRESENT and is removed");
            Long currRank = ef1.employeeMap.get(remId).getEmployeeRank();

            ef1.employeeMap.remove(remId);
            ef1.rankMap.get(currRank).remove(remId);
        }
        else {
            pw.println("Emp is not present");
        }

        pw.close();
    }
}
