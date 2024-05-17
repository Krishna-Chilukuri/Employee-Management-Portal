package employee.servlets;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.io.PrintWriter;

import static employee.servlets.AddEmployeeServlet.addEmployee;

class insertionThread extends Thread {
    private int offset;
    private int numOfInsertions;
    insertionThread(int offset, int numOfInsertions) {
        this.offset = offset;
        this.numOfInsertions = numOfInsertions;
    }
    public void run() {
        long curr = this.offset;

        while(curr <= this.numOfInsertions) {
            //Insertion Process
        }
    }
}

public class InsertMultiEmployees  extends HttpServlet {
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        PrintWriter pw = res.getWriter();
        int numOfThreads = 4;

        for (int i = 1; i <= numOfThreads; i++) {
            insertionThread th = new insertionThread(i, 4);
            th.start();
        }

    }
}
