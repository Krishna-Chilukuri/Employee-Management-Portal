package employee.servlets;

import employee.factory.EmployeeFactory;
import employee.model.Employee;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import static employee.servlets.AddEmployeeServlet.addEmployee;

public class TraditionalMultiInserts extends HttpServlet {
    public void loopingInsert(long numOfEmps, PrintWriter pw) {
        EmployeeFactory ef1 = EmployeeFactory.getInstance();
        for (long i = 1; i <= numOfEmps; i++) {
            Employee newEmp = new Employee();
            newEmp.setEmployeeId(i);
            newEmp.setEmployeeName(randomNames.randomName());
            long rank = 1L;
            while (true) {
                if (!ef1.rankMap.containsKey(rank)) {
                    newEmp.setEmployeeRank(rank);
                    break;
                }
                if (ef1.rankMap.get(rank).size() < rank) {
                    newEmp.setEmployeeRank(rank);
                    break;
                }
                rank++;
            }
            addEmployee(newEmp, pw);
        }
    }
    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        PrintWriter pw = res.getWriter();
        Enumeration<String> e = req.getParameterNames();
        long startTime = System.currentTimeMillis();
        long numOfEmps = -1;
        while(e.hasMoreElements()) {
            String pname = e.nextElement();
            if (pname.equals("countOfEmps")) {
                numOfEmps = Long.parseLong(req.getParameter(pname));
            }
        }
        if (numOfEmps < 0) {
            pw.println("Num of Employees to be inserted can't be -ve");
            return;
        }
        loopingInsert(numOfEmps, pw);
        long finishTime = System.currentTimeMillis();
        pw.println("Time to Insert " + numOfEmps +" Employees : " + (finishTime - startTime));
        pw.close();
    }
}
