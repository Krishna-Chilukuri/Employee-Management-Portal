package employee.servlets;

import employee.factory.DBFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Enumeration;

public class LoginServlet extends HttpServlet {
    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        PrintWriter pw = res.getWriter();
        String username = "", password = "";

        Enumeration<String> e = req.getParameterNames();
        while (e.hasMoreElements()) {
            String pname = e.nextElement();
            if (pname.equals("username")) username = req.getParameter(pname);
            if (pname.equals("userpassword")) password = req.getParameter(pname);
        }

        if (username.isEmpty() || password.isEmpty()) {
            pw.println("Username and password are needed");
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
            RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/pages/homePage.jsp");
            rd.forward(req, res);

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
