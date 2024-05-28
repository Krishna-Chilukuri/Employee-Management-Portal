package employee.servlets;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

public class LogOutServlet extends HttpServlet {
    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Cookie userDetails = new Cookie("user", "");
        userDetails.setMaxAge(0);
        PrintWriter pw = res.getWriter();
        pw.println("Log out Done");
        res.addCookie(userDetails);
    }
}
