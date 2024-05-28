package employee.servlets;

import employee.factory.DBFactory;

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

public class ViewAdminOwner extends HttpServlet {
    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        PrintWriter pw = res.getWriter();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            DBFactory dbf = DBFactory.getInstance();
            Connection conn = DriverManager.getConnection(dbf.url, dbf.username, dbf.password);
            PreparedStatement stmt = conn.prepareStatement("select * from login where privilege = ? or privilege = ?");
            stmt.setString(1, "admin");
            stmt.setString(2, "owner");
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                pw.println(rs.getString("username") + "->" + rs.getString("privilege"));
            }
        }
        catch (Exception e) {
            pw.println("Caught Exception : " + e);
        }
        pw.close();
    }
}
