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

    static boolean addToHierarchy(HashMap<Long, Employee> employeeHashMap, HashMap<Long, List<Long>> rankHashMap, Employee testEmp) {
        Random randGen = new Random();
        Long currSuperiorRank = testEmp.getEmployeeRank() - 1;
        while (!rankHashMap.containsKey(currSuperiorRank) || (rankHashMap.containsKey(currSuperiorRank) && rankHashMap.get(currSuperiorRank).isEmpty())) {
            if (currSuperiorRank == 0) break;
            currSuperiorRank--;
        }
        testEmp.setReportees(new ArrayList<>());

        if (currSuperiorRank > 0) {
            //Superior unnadu
//            Long superior = (long) (Math.random() * (currSuperiorRank - 1));
            Long superior = rankHashMap.get(currSuperiorRank).get(randGen.nextInt(rankHashMap.get(currSuperiorRank).size()));
//                    .get(randGen.nextLong() % currSuperiorRank);
//            rebalanceHierarchy(employeeHashMap, testEmp);
            employeeHashMap.get(superior).addReportee(testEmp);
            testEmp.setReportsTo(employeeHashMap.get(superior));
        }

        employeeHashMap.put(testEmp.getEmployeeId(), testEmp);
        rankHashMap.get(testEmp.getEmployeeRank()).add(testEmp.getEmployeeId());
        return true;
    }

    static void rebalanceHierarchy(HashMap<Long, Employee> employeeHashMap, Employee newEmp) {
        Employee currBoss = newEmp.getReportsTo();
        if (currBoss == null) return;
        long newEmpRank = newEmp.getEmployeeRank();

//        List<Employee> newEmpReportees = new ArrayList<Employee>();
        List<Employee> currBossReportees = currBoss.getReportees();
        List<Long> changeIDs = new ArrayList<>();

        for(Employee currReportee : currBossReportees) {
            if (currReportee.getEmployeeRank() > newEmpRank) {
                changeIDs.add(currReportee.getEmployeeId());
            }
        }

        for (Long changeID : changeIDs) {
            Employee currRep = employeeHashMap.get(changeID);
            currBoss.removeReportee(currRep);
            currRep.setReportsTo(newEmp);
            newEmp.addReportee(currRep);
        }
//
//        for (Employee currReportee : currBossReportees) {
//            if (currReportee.getEmployeeRank() > newEmpRank) {
//                currBoss.removeReportee(currReportee);
//                currReportee.setReportsTo(newEmp);
//                newEmp.addReportee(currReportee);
//            }
//            newEmp.setReportees(newEmpReportees);
//        }
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

        //is Employee being inserted a duplicate
        if (isDuplicateEmployee(ef1.employeeMap, newEmp)) {
            pw.println("Duplicate Employee, Insertion not Possible");
            return;
        }
        //does the rank provided align with the hierarchy
        if (isRankNotPossible(ef1.rankMap, newEmp)) {
            pw.println("Insertion in rank is not possible");
            pw.println(ef1.rankMap.get(newEmp.getEmployeeRank()).size());
            return;
        }

        if (!addToHierarchy(ef1.employeeMap, ef1.rankMap, newEmp)) {
            pw.println("Hierarchy can't be set");
        }
        rebalanceHierarchy(ef1.employeeMap, newEmp);
        pw.println("Employee Added");
        pw.println("ID : " + newEmp.getEmployeeId());
        pw.println("Name : " + newEmp.getEmployeeName());
        pw.println("Rank : " + newEmp.getEmployeeRank());
        pw.println("RANK CURR LEN : " + ef1.rankMap.get(newEmp.getEmployeeRank()).size());

//        if (!addToHierarchy(ef1.employeeMap, ef1.rankMap, newEmp)) {
//            pw.println("Hierarchy can't be set");
//        }
//        else {
//        }

        pw.println("SIZE : " + ef1.employeeMap.get(newEmp.getEmployeeId()).getReportees().size());
        pw.close();

    }
}
