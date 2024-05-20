package employee.servlets;

import employee.factory.EmployeeFactory;
import employee.model.Employee;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class UpperHierarchyServlet extends HttpServlet {
    static boolean isIdAvailable(HashMap<Long, Employee> empMap, Long empId) {
        return empMap.containsKey(empId);
    }
    //Boss access constant
    static Employee findBoss(HashMap<Long, Employee> empMap, Long empId) {
        Employee currEmp = empMap.get(empId);

        while (currEmp.getReportsTo() != null) {
            currEmp = currEmp.getReportsTo();
        }

        return currEmp;
    }
    static void hierarchyUptoRank(Employee boss, Long targetId, Long targetRank, PrintWriter pw) {
        Employee curr = boss;
        Employee repo;
        Queue<Employee> empQ = new LinkedList<>();
        List<Employee> repList = new ArrayList<>();
        ListIterator<Employee> repListIter;
        empQ.add(curr);
        while (empQ.peek() != null && empQ.peek().getEmployeeRank() <= targetRank) {
            curr = empQ.poll();
            pw.println("EMP : " + curr.getEmployeeId() + ' ' + curr.getEmployeeName() + ' ' + curr.getEmployeeRank());
            repList = curr.getReportees();
            repListIter = repList.listIterator();
            while (repListIter.hasNext()) {
                repo = repListIter.next();
                if (repo.getEmployeeRank() == targetRank && repo.getEmployeeId() != targetId) continue;
                empQ.add(repo);
            }
        }
    }
    //Target rank lo only 1 kaabatti dekho
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        Enumeration<String> e = req.getParameterNames();
        EmployeeFactory ef1 = EmployeeFactory.getInstance();
        long empId = -1;
        PrintWriter pw = res.getWriter();

        while(e.hasMoreElements()) {
            String pname = e.nextElement();
            if (pname.equals("eid")) {
                empId = Long.parseLong(req.getParameter(pname));
            }
        }

        pw.println("Upper Hierarchy for " + empId);
        if (!isIdAvailable(ef1.employeeMap, empId)) {
            pw.println("ID is not available");
            return;
        }
        Employee currBoss = findBoss(ef1.employeeMap, empId);
        pw.println("CUrr Tree Boss : " + currBoss.getEmployeeId() + currBoss.getEmployeeName() + currBoss.getEmployeeRank());

        hierarchyUptoRank(currBoss, empId, ef1.employeeMap.get(empId).getEmployeeRank(), pw);
    }
}
