package employee.servlets;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import static employee.servlets.AddEmployeeServlet.readCookie;
import static employee.servlets.PromoteToAdmin.checkIfOwner;
import static employee.servlets.PromoteToAdmin.updateUser;

public class DemoteOwner extends HttpServlet {
    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        PrintWriter pw = res.getWriter();
        Enumeration<String> e = req.getParameterNames();
        long empId = 0L;
        while(e.hasMoreElements()) {
            String pname = e.nextElement();
            if (pname.equals("empId")) {
                empId = Long.parseLong(req.getParameter(pname));
            }
        }
        if (empId == 0) {
            pw.println("Employee id needed");
            return;
        }
        String reqUserId = readCookie((HttpServletRequest) req, "user");
        pw.println("req User ID is : " + reqUserId);

        if (checkIfOwner(reqUserId, empId, pw, (HttpServletResponse) res)) {
            updateUser(empId, "admin", pw);
        }
    }
}
