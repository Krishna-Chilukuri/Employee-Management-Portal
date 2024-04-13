package employee.servlets;

import employee.factory.EmployeeFactory;
import employee.model.Employee;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class AddEmployeeServlet extends HttpServlet {

    static boolean isDuplicateEmployee(HashMap<Long, Employee> employeeHashMap, Employee testEmp) {
        return employeeHashMap.containsKey(testEmp.getEmployeeId());
    }

    static boolean isRankNotPossible(HashMap<Long, List<Long>> rankHashMap, Employee testEmp) {
        long checkRank = testEmp.getEmployeeRank();
        if (!rankHashMap.containsKey(checkRank)) rankHashMap.put(checkRank, new ArrayList<Long>());
        return checkRank <= rankHashMap.get(checkRank).size();
    }

    static boolean isHierarchySet(HashMap<Long, Employee> employeeHashMap, HashMap<Long, List<Long>> rankHashMap, Employee testEmp) {
        Random rand = new Random();
        long supRank = testEmp.getEmployeeRank() - 1;
        if (supRank == 0) {
            //Boss insertion
            employeeHashMap.put(testEmp.getEmployeeId(), testEmp);
            rankHashMap.get(testEmp.getEmployeeRank()).add(testEmp.getEmployeeId());
            return true;
        }

        List<Long> superiorIds = rankHashMap.get(supRank);
        superiorIds.get(rand.nextInt())
        for (long supId: superiorIds) {

        }
        return false;
    }

    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        EmployeeFactory ef1 = EmployeeFactory.getInstance();
        PrintWriter pw = res.getWriter();
        Employee newEmp = new Employee();
        long higherRank;
        boolean insertionStat = false;

        Enumeration<String> e = req.getParameterNames();
        while(e.hasMoreElements()) {
            String pname = e.nextElement();
            if (pname.equals("eid")) {
                newEmp.setEmployeeId(Long.parseLong(req.getParameter(pname)));
            }
            else if(pname.equals("ename")) {
                newEmp.setEmployeeName(req.getParameter(pname));
            }

            else if(pname.equals("erank")) {
                newEmp.setEmployeeRank(Long.parseLong(req.getParameter(pname)));
            }
        }


        if (isDuplicateEmployee(ef1.employeeMap, newEmp)) {
            pw.println("Duplicate Employee, Insertion not Possible");
            return;
        }
        if (isRankNotPossible(ef1.rankMap, newEmp)) {
            pw.println("Insertion in rank is not possible");
            pw.println(ef1.rankMap.get(newEmp.getEmployeeRank()).size());
            return;
        }
        if (!isHierarchySet(ef1.employeeMap, ef1.rankMap, newEmp)) {
            pw.println("Hierarchy can't be set");
        }
        else {
            pw.println("Employee Added");
            pw.println("ID : " + newEmp.getEmployeeId());
            pw.println("Name : " + newEmp.getEmployeeName());
            pw.println("Rank : " + newEmp.getEmployeeRank());
            pw.println("RANK CURR LEN : " + ef1.rankMap.get(newEmp.getEmployeeRank()).size());
        }

        pw.close();

    }
}
