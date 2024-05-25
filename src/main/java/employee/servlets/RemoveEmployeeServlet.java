package employee.servlets;

import employee.exception.PromoteException;
import employee.factory.DBFactory;
import employee.factory.EmployeeFactory;
import employee.model.Employee;
import jdk.internal.net.http.RequestPublishers;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.*;

import static employee.servlets.AddEmployeeServlet.checkPrivilege;
import static employee.servlets.AddEmployeeServlet.readCookie;
import static employee.servlets.PromoteServlet.getEmployeeRank;
import static employee.servlets.PromoteServlet.promoteEmp;

public class RemoveEmployeeServlet extends HttpServlet {
    static long isIdAvailable(Connection conn, long empId) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("select * from employees where employee_id = ?");
        stmt.setLong(1, empId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getLong("employee_rank");
        }
        return -1;
    }

    //Split into multiple functions
//    static void handleReportees(HashMap<Long, Employee> empMap, HashMap<Long, List<Long>> rankMap, Employee remEmp) {
//        if (remEmp.getReportees().isEmpty()) return;
//        Random rand = new Random();
//        List<Employee> remReportees = remEmp.getReportees();
//        long remRank = remEmp.getEmployeeRank();
//        Long remId = remEmp.getEmployeeId();
//        List<Long> currRankIds = null;
//
//        while(remRank > 0) {
//            currRankIds = rankMap.get(remRank);
//            if (currRankIds.isEmpty() ) {
//                remRank--;
//                continue;
//            }
//            break;
//        }
//        if (currRankIds == null || currRankIds.isEmpty()) {
////            Random rand = new Random();
////            int randomElement = givenList.get(rand.nextInt(givenList.size()));
//            int randIdx = rand.nextInt(remReportees.size());
//            Employee newBoss = remReportees.get(randIdx);
//            rankMap.get(newBoss.getEmployeeRank()).remove(newBoss.getEmployeeId());
//            newBoss.setEmployeeRank(newBoss.getEmployeeRank() - 1);
//            rankMap.get(newBoss.getEmployeeRank()).add(newBoss.getEmployeeId());
//            newBoss.setReportsTo(null);
//            remReportees.remove(randIdx);
//            for (Employee reportee: remReportees) {
//                reportee.setReportsTo(newBoss);
//                newBoss.addReportee(reportee);
//            }
//            return;
//        }
//        int remIdx = 0;
////        Long currRankId;
//        ListIterator<Long> currRankId = currRankIds.listIterator();
//        while (remIdx < remReportees.size()) {
//            //get a curr rank id boss
//            Employee remRepo = empMap.get(remReportees.get(remIdx++).getEmployeeId());
//            if (!currRankId.hasNext()) {
//                currRankId = currRankIds.listIterator();
//            }
//            Employee newBoss = empMap.get(currRankId.next());
//            newBoss.addReportee(remRepo);
//            remRepo.setReportsTo(newBoss);
//        }
//    }
    static void handleReportees(Connection conn, long remId, long remRank, PrintWriter pw) throws SQLException, PromoteException {
        //if no reportees return back
        Random randgen = new Random();
        PreparedStatement stmt = conn.prepareStatement("select reportee from reportees where employee_id = ?");
        stmt.setLong(1, remId);
        ResultSet rs = stmt.executeQuery();
        List<Long> remReporteesIds = new ArrayList<>();
        List<Long> currRankIds = new ArrayList<>();
        long currRemRank;
        while (rs.next()) {
            remReporteesIds.add(rs.getLong("reportee"));
        }
        if (remReporteesIds.isEmpty()) return;
        stmt = conn.prepareStatement("select employee_id from employees where employee_rank = ? and employee_id != ?");
        stmt.setLong(2, remId);
        while (remRank > 0) {
            stmt.setLong(1, remRank);
            rs = stmt.executeQuery();
//            int currCount = 0;
            while (rs.next()) {
                currRankIds.add(rs.getLong("employee_id"));
//                currCount += 1;
            }
            if (currRankIds.isEmpty()) remRank--;
            else break;
        }
        pw.println("Superior rank : "+ remRank);
        if (remRank > 0) {
            //We got a superiors
            int remIdx = 0;
            ListIterator<Long> currRankId = currRankIds.listIterator();
            while (remIdx < remReporteesIds.size()) {
                long currRepId = remReporteesIds.get(remIdx++);
                if (!currRankId.hasNext()) currRankId = currRankIds.listIterator();
                long currBossId = currRankId.next();
                //update employee_id to currBossId for currRepId in reportees
                //update reports_to to currBossId for currRepId in employees
                stmt = conn.prepareStatement("update reportees set employee_id = ? where reportee = ?");
                stmt.setLong(1, currBossId);
                stmt.setLong(2, currRepId);
                if (stmt.executeUpdate() == 1) {
                    pw.println("Update done");
                }
                else {
                    throw new SQLException("Update Error in reportees");
                }
                stmt = conn.prepareStatement("update employees set reports_to = ? where employee_id = ?");
                stmt.setLong(1, currBossId);
                stmt.setLong(2, currRepId);
                if (stmt.executeUpdate() == 1) {
                    pw.println("Update Done");
                }
                else {
                    throw new SQLException("Update Error in employees");
                }
            }
        }
        else {
//            promote a reportee by 1 rank and use this reportee as boss
            long newBoss = remReporteesIds.get(randgen.nextInt(remReporteesIds.size()));
            remReporteesIds.remove(newBoss);
//            promoteEmp(newBoss, pw);
            //promote newBoss by 1 rank
            long bRank = isIdAvailable(conn, newBoss);
            stmt = conn.prepareStatement("insert into rankCounts (rankNum, rankCount) VALUES (?, 1) on duplicate key update rankCount = rankCount - 1");
            stmt.setLong(1, bRank);
            pw.println("RANK MAP UPDATE : " + stmt.executeUpdate());
//            if (stmt.executeUpdate() == 1) {
//                pw.println("Rank Count table updated in rem 1");
//            }
//            else {
//                throw new SQLException("Error while updating rank count map 1");
//            }
            bRank--;
            stmt = conn.prepareStatement("insert into rankCounts (rankNum, rankCount) VALUES (?, 1) on duplicate key update rankCount = rankCount + 1");
            stmt.setLong(1, bRank);
            pw.println("RANK MAP UPDATE: " + stmt.execute());
//            if (stmt.executeUpdate() == 1) {
//                pw.println("Rank Count table updated in rem 2");
//            }
//            else {
//                throw new SQLException("Error while updating rank count map 2");
//            }
            stmt = conn.prepareStatement("update employees set employee_rank = employee_rank - 1, reports_to = null where employee_id = ?");
//            stmt.setLong(1, currRemRank - 1);
            stmt.setLong(1, newBoss);
            if (stmt.executeUpdate() == 1) {
                pw.println("Update DOne");
            }
            else {
                throw new SQLException("Update Error in employees while promoting one of removing reportees");
            }
            for (long remReportee: remReporteesIds) {
                //update employee_id to newBoss for remReportee in reportees
                //update reports_to to newBoss for remReportee in employees
                stmt = conn.prepareStatement("update reportees set employee_id = ? where reportee = ?");
                stmt.setLong(1, newBoss);
                stmt.setLong(2, remReportee);
                if (stmt.executeUpdate() == 1) {
                    pw.println("Update done");
                }
                else {
                    throw new SQLException("Update Error in reportees");
                }
                stmt = conn.prepareStatement("update employees set reports_to = ? where employee_id = ?");
                stmt.setLong(1, newBoss);
                stmt.setLong(2, remReportee);
                if (stmt.executeUpdate() != 0) {
                    pw.println("Update Done");
                }
                else {
                    throw new SQLException("Update Error in employees");
                }
            }
        }
    }
    //Check for remId in empMap
    static boolean removeEmployee(Long remId, PrintWriter pw) {
        DBFactory dbf = DBFactory.getInstance();
        Connection conn;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(dbf.url, dbf.username, dbf.password);
            long remRank = isIdAvailable(conn, remId);
            if (remRank == -1) {
                pw.println("ID not available to remove");
                return false;
            }
            //ID Is present
            handleReportees(conn, remId, remRank, pw);
            //handle reportees
            PreparedStatement stmt = conn.prepareStatement("delete from employees where employee_id = ?");
            stmt.setLong(1, remId);
            if (stmt.executeUpdate() == 1) {
                pw.println("Deletion DONE");
            }
            else {
                throw new SQLException("Deletion from employees error");
            }
            stmt = conn.prepareStatement("delete from reportees where reportee = ? or employee_id = ?");
            stmt.setLong(1, remId);
            stmt.setLong(2, remId);
            if (stmt.executeUpdate() == 1) {
                pw.println("Deletion DOne");
            }
            else {
                pw.println(remId + " is not a reportee of any employee");
//                throw new SQLException("Deletion from reportees error");
            }
            //reportees lo reportee == remEmpid record deletion
            //remEmpid record deletion
            stmt = conn.prepareStatement("insert into rankCounts (rankNum, rankCount) VALUES (?, 1) on duplicate key update rankCount = rankCount - 1");
            stmt.setLong(1, remRank);
            pw.println("RANK COUNR UPDATE : " + stmt.executeUpdate());
//            if (stmt.executeUpdate() == 1) {
//                pw.println("Rank Count table updated");
//            }
//            else {
//                throw new SQLException("Error while updating rank count map");
//            }
            pw.println("POST RANK COUNT UPDATION");
        }
        catch (Exception e) { pw.println("Caught Exception : " + e); }
        return true;
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
        long remRank = getEmployeeRank(remId);
        String reqUserId = readCookie((HttpServletRequest) req, "user");
        pw.println("Req user id : " + reqUserId);

        if (!checkPrivilege(reqUserId, remRank, pw, (HttpServletResponse) res)) {
            pw.println("You can't remove this employee");
            return;
        }

        removeEmployee(remId, pw);

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
