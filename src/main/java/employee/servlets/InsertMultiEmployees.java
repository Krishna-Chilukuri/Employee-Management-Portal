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

import static employee.servlets.AddEmployeeServlet.addEmployee;

class randomNames {
    private static List<String> namesList = Arrays.asList("Rama", "Krishna", "Kaantha", "Teja", "Prudhvi", "Sai", "John", "Peter", "Chilu", "KP", "Srinu", "Raj", "Ravi", "Ramesh", "Eluri", "Suresh");

    static String randomName() {
        Random rand = new Random();
        int randIdx = rand.nextInt(namesList.size());
        return namesList.get(randIdx);
    }

}

class insertionThread extends Thread {
    private long offset;
    private long numOfInsertions;
    private PrintWriter pw;
    insertionThread(int offset, int numOfInsertions, PrintWriter pw) {
        this.offset = offset;
        this.numOfInsertions = numOfInsertions;
        this.pw = pw;
    }
    public void run() {
        long curr = (this.offset * this.numOfInsertions) - this.numOfInsertions + 1;
//        boolean breakStat = false;
        EmployeeFactory ef1 = EmployeeFactory.getInstance();

//        this.pw.println("IN RUN : " + curr + " -> " +(this.offset * this.numOfInsertions));
        synchronized (ef1) {
            while (this.numOfInsertions > 0) {
                //Insertion Process
                Employee newEmp = new Employee();
                newEmp.setEmployeeId(curr);
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
                addEmployee(newEmp, this.pw);
                curr++;
                this.numOfInsertions--;
            }
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
        for (int i = 1; i <= numOfThreads; i++) {
            pw.println("IN SERVICE FOR " + i);
            insertionThread th = new insertionThread(i, numOfInsertions, pw);
            th.start();
        }
        long finishTime = System.currentTimeMillis();
        pw.println("Time to Insert " + numOfEmps +" Employees : " + (finishTime - startTime));
        pw.close();
    }
}
