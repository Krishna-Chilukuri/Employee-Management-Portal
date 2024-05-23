package employee.servlets;

import employee.factory.DBFactory;
import employee.factory.EmployeeFactory;
import employee.model.Employee;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.*;
import java.util.concurrent.Semaphore;

import static employee.servlets.AddEmployeeServlet.addEmployee;
import static employee.servlets.ViewAllEmployees.getEmployees;

class randomNames {
    private static List<String> namesList = Arrays.asList("Rama", "Krishna", "Kaantha", "Teja", "Prudhvi", "Sai", "John", "Peter", "Chilu", "KP", "Srinu", "Raj", "Ravi", "Ramesh", "Eluri", "Suresh");

    static String randomName() {
        Random rand = new Random();
        int randIdx = rand.nextInt(namesList.size());
        return namesList.get(randIdx);
    }

}

class mutexSemaphone {
    static final Semaphore mutexSem = new Semaphore(1);

//    private mutexSemaphone() {
//        this.mutexSem = new Semaphore(1);
//    }

    public static Semaphore getInstance() {
        return mutexSem;
    }
}

class insertionThread extends Thread {
    private long offset;
    private long numOfInsertions;
    private Connection conn;
    private PrintWriter pw;
//    private final Semaphore mutexSem = new Semaphore(1);
    insertionThread(int offset, int numOfInsertions, Connection conn, PrintWriter pw) {
        this.offset = offset;
        this.numOfInsertions = numOfInsertions;
        this.conn = conn;
        this.pw = pw;
    }
    public synchronized void run() {
        try {
            mutexSemaphone.mutexSem.acquire();
//            mutexSemaphone sem = mutexSemaphone.getInstance();
            long curr = (this.offset * this.numOfInsertions) - this.numOfInsertions + 1;
//        boolean breakStat = false;
            EmployeeFactory ef1 = EmployeeFactory.getInstance();
            boolean breakStat = false;

//        this.pw.println("IN RUN : " + curr + " -> " +(this.offset * this.numOfInsertions));
//        synchronized () {
            pw.println("==================\nGOT SEMAPHORE " + this.offset + "\n=====================");
            while (this.numOfInsertions > 0) {
                //Insertion Process
                Employee newEmp = new Employee();
                newEmp.setEmployeeId(curr);
                newEmp.setEmployeeName(randomNames.randomName());
                long rank = 0;
                try {
                    PreparedStatement stmt = this.conn.prepareStatement("select rankNum, rankCount from rankCounts order by rankNum ASC");
                    ResultSet rs = stmt.executeQuery();
//                    if (rs == null) {
//                        newEmp.setEmployeeRank(rank);
//                        breakStat = true;
//                    }
                    while(rs.next()) {
                        rank = rs.getLong("rankNum");
                        long currCount = rs.getLong("rankCount");
                        if (rank > currCount) {
                            newEmp.setEmployeeRank(rank);
                            breakStat = true;
                            break;
                        }
                    }
                    if (!breakStat) {
                        pw.println("FALSE BREAKSTAT for " + rank);
//                        if (rank > 1) rank++;
                        rank++;
                        newEmp.setEmployeeRank(rank);
                    }
                    pw.println("Before adding " + newEmp.getEmployeeId());
                    getEmployees(pw);
                    pw.println("RANK : " + rank);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

//                while (true) {
//                    if (!ef1.rankMap.containsKey(rank)) {
//                        newEmp.setEmployeeRank(rank);
//                        break;
//                    }
//                    if (ef1.rankMap.get(rank).size() < rank) {
//                        newEmp.setEmployeeRank(rank);
//                        break;
//                    }
//                    rank++;
//                }
                pw.println("TRYING TO ADD" + newEmp);
                addEmployee(newEmp, this.pw);
                pw.println("EMPLOYEE INSERTED : " + newEmp.getEmployeeId());
                curr++;
                this.numOfInsertions--;
//            }
            }
        } catch (InterruptedException e) {
            pw.println("==================\nINTERRUPT CAUSED IN TRY OF THREAD\n====================");
            throw new RuntimeException(e);
        }
        finally {
            pw.println("=================\n SEMAPHORE RELEASED\n=================");
            mutexSemaphone.mutexSem.release();
        }
    }
}

public class InsertMultiEmployees  extends HttpServlet {
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
        int numOfThreads = 4;
        int numOfInsertions = (int) (numOfEmps / numOfThreads);
        pw.println("NUM OF INSERTIONS: " + numOfInsertions);
        Connection conn;
        DBFactory dbf = DBFactory.getInstance();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(dbf.url, dbf.username, dbf.password);
            for (int i = 1; i <= numOfThreads; i++) {
                pw.println("IN SERVICE FOR " + i);
                insertionThread th = new insertionThread(i, numOfInsertions, conn, pw);
                th.start();
            }
        }
        catch (Exception ex) { pw.println("Error Caught for threads: " + ex); }
        long finishTime = System.currentTimeMillis();
        pw.println("Time to Insert " + numOfEmps +" Employees : " + (finishTime - startTime));
        pw.close();
    }
}
