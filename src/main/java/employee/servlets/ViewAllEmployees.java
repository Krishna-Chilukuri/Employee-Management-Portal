package employee.servlets;

import employee.factory.EmployeeFactory;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.io.PrintWriter;

public class ViewAllEmployees extends HttpServlet {
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        EmployeeFactory ef1 = EmployeeFactory.getInstance();
        PrintWriter pw = res.getWriter();

        ef1.employeeMap.forEach((key, val) ->
                pw.println(key + " => " + val.getEmployeeId() + " " + val.getEmployeeName() + " " + val.getEmployeeRank() + " " + val.getReportsTo() + " " + val.getReportees() + " " +val));

        ef1.rankMap.forEach((key, val) ->
                pw.println(key + " rank => " + val));

        pw.close();
    }
}
