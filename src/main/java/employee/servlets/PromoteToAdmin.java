package employee.servlets;

import employee.factory.DBFactory;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Enumeration;

import static employee.servlets.AddEmployeeServlet.readCookie;
import static employee.servlets.RemoveEmployeeServlet.removeEmployee;

public class PromoteToAdmin extends HttpServlet {
    static boolean checkIfOwner(String reqUserId, long empId, PrintWriter pw, HttpServletResponse res) {
        Connection conn;
        DBFactory dbf = DBFactory.getInstance();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(dbf.url, dbf.username, dbf.password);
            PreparedStatement stmt = conn.prepareStatement("select privilege from login where username = ?");
            stmt.setString(1, reqUserId);
            ResultSet rs = stmt.executeQuery();
            if (!rs.next()) {
                pw.println("No user of your ID found");
                Cookie userDetails = new Cookie("user", "");
                userDetails.setMaxAge(0);
                res.addCookie(userDetails);
                return false;
            }
            String privilege = rs.getString("privilege");
            if (privilege.equals("owner")) {
                stmt = conn.prepareStatement("select * from login where username = ?");
                stmt.setString(1, String.valueOf(empId));
                rs = stmt.executeQuery();
                if (!rs.next()) return false;
                return true;
            }
        }
        catch (Exception ex) {
            pw.println("Caught Exception : " + ex);
        }
        return false;
    }
    static void updateUser(long empId, String privilege, PrintWriter pw) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            DBFactory dbf = DBFactory.getInstance();
            Connection conn = DriverManager.getConnection(dbf.url, dbf.username, dbf.password);
            PreparedStatement stmt = conn.prepareStatement("update login set privilege = ? where username = ?");
            stmt.setString(1, privilege);
            stmt.setString(2, String.valueOf(empId));
            stmt.executeUpdate();
        }
        catch (Exception e) {
            pw.println("Caught Exception : " + e);
        }
    }
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
        pw.println("Req user id is : " + reqUserId);

        if (checkIfOwner(reqUserId, empId, pw, (HttpServletResponse) res)) {
            removeEmployee(empId, pw);
            updateUser(empId, "admin", pw);
        }
        pw.close();
    }
}
