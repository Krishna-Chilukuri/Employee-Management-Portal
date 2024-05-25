package employee.servlets;

import employee.factory.DBFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Enumeration;

public class LoginServlet extends HttpServlet {
    @Override
    public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter pw = res.getWriter();
        String username = "", password = "";

        Enumeration<String> e = req.getParameterNames();
        while (e.hasMoreElements()) {
            String pname = e.nextElement();
            if (pname.equals("username")) username = req.getParameter(pname);
            if (pname.equals("userpassword")) password = req.getParameter(pname);
        }

//        HttpSession session = req.getSession(false);
//        if (session != null) {
//            pw.println("Welcome Miter " + session.getAttribute("user"));
//            RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/pages/homePage.jsp");
//            rd.forward(req, res);
//        }


        if (username.isEmpty() || password.isEmpty()) {
//            pw.println("Username and password are needed");
            RequestDispatcher rd = req.getRequestDispatcher("index.jsp");
            rd.forward(req, res);
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            DBFactory dbf = DBFactory.getInstance();
            Connection conn = DriverManager.getConnection(dbf.url, dbf.username, dbf.password);

            PreparedStatement stmt = conn.prepareStatement("select privilege from login where username = ? and password = ?");
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                //LOgin Failed
//                pw.println("login Failed");
                RequestDispatcher rd = req.getRequestDispatcher("index.jsp");
                rd.forward(req, res);
                return;
            }
            String privilege = rs.getString("privilege");
//            pw.println("Login Successful");
//            session = req.getSession();
//            session.setAttribute("user", username);
//            session.setAttribute("privilege", rs.getString("privilege"));
            Cookie userDetails = new Cookie("user", username);
            userDetails.setMaxAge(60 * 60);
//            userDetails.setDomain("localhost:8080/untitled2");
            res.addCookie(userDetails);
            RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/pages/homePage.jsp");
//            res.sendRedirect("/WEB-INF/pages/homePage.jsp");
            rd.include(req, res);   
            rd.forward(req, res);

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
