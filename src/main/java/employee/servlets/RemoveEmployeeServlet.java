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

public class RemoveEmployeeServlet extends HttpServlet {
    static boolean isIdAvailable(HashMap<Long, Employee> empMap, Long remId) {
        return empMap.containsKey(remId);
    }

    static void handleReportees(HashMap<Long, Employee> empMap, HashMap<Long, List<Long>> rankMap, Employee remEmp) {
        if (remEmp.getReportees().isEmpty()) return;
        List<Employee> remReportees = remEmp.getReportees();
        Long remRank = remEmp.getEmployeeRank();
        Long remId = remEmp.getEmployeeId();
        List<Long> currRankIds = new ArrayList<>();

        rankMap.get(remRank).remove(remId);
        while(remRank > 0) {
            currRankIds = rankMap.get(remRank);
            if (currRankIds.isEmpty() ) {
                remRank--;
                continue;
            }
            break;
        }
//        if (currRankIds.isEmpty()) {
            //Promote some reportee to the removal rank and add the reportees to it
//        }
        int remIdx = 0;
//        Long currRankId;
        ListIterator<Long> currRankId = currRankIds.listIterator();
        while (remIdx < remReportees.size()) {
            //get a curr rank id boss
            Employee remRepo = empMap.get(remReportees.get(remIdx++).getEmployeeId());
            if (!currRankId.hasNext()) {
                currRankId = currRankIds.listIterator();
            }
            Employee newBoss = empMap.get(currRankId.next());
            newBoss.addReportee(remRepo);
            remRepo.setReportsTo(newBoss);
        }
    }
    static void removeEmployee(Long remId) {
        EmployeeFactory ef1 = EmployeeFactory.getInstance();
        Employee remEmp = ef1.employeeMap.get(remId);
        handleReportees(ef1.employeeMap, ef1.rankMap, remEmp);
        ef1.employeeMap.get(remEmp.getReportsTo().getEmployeeId()).removeReportee(remEmp);
        ef1.employeeMap.remove(remEmp.getEmployeeId());
    }
    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        EmployeeFactory ef1 = EmployeeFactory.getInstance();
        Enumeration<String> e = req.getParameterNames();

        long remId = -1;
        PrintWriter pw = res.getWriter();

        while(e.hasMoreElements()) {
            String pname = e.nextElement();
            if (pname.equals("remid")) {
                remId = Long.parseLong(req.getParameter(pname));
            }
        }

        if (!isIdAvailable(ef1.employeeMap, remId)) {
            pw.println("ID not available to remove");
            return;
        }

        pw.println("REmoval DONE!!!");
//
//        if (ef1.employeeMap.containsKey(remId)) {
//            //Removal Process
//            Long superiorId = ef1.employeeMap.get(remId).getReportsTo().getEmployeeId();
//            Long coRank = ef1.employeeMap.get(remId).getEmployeeRank();
//
//            ef1.rankMap.get(coRank).remove(remId);
//
//
//            pw.println("EMP IS PRESENT and is removed");
//            Long currRank = ef1.employeeMap.get(remId).getEmployeeRank();
//
//            ef1.employeeMap.remove(remId);
//            ef1.rankMap.get(currRank).remove(remId);
//        }
//        else {
//            pw.println("Emp is not present");
//        }

        pw.close();
    }
}
