package employee.servlets;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

public class LoginTrackerServlet extends HttpServlet {
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter pw = res.getWriter();
        pw.println("BEFORE GETTING SESSION");
        HttpSession session = req.getSession(false);
        pw.println("BEFORE TRY");
        try {
            pw.println("Before if");
            if (session == null) {
                pw.println("In if 1");
                RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/index.jsp");
                pw.println("In if 2");
                rd.forward(req, res);
            } else {
                pw.println("In else 1");
                pw.println("Welcome Miter " + session.getAttribute("user"));
                pw.println("In else 2");
                RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/pages/homePage.jsp");
                pw.println("In else 3");
                rd.forward(req, res);
            }
        }
        catch (Exception e) {
            pw.println("Caught : " + e);
        }
    }
}
