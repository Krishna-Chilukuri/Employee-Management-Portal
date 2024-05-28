package employee.factory;

import employee.model.Employee;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class EmployeeFactory {
    public HashMap<Long, Employee> employeeMap;
    public HashMap<Long, List<Long>> rankMap;

    public static EmployeeFactory ef1 = new EmployeeFactory();

    private EmployeeFactory() {
        this.employeeMap = new HashMap<>();
        this.rankMap = new HashMap<>();
    }

    public static synchronized EmployeeFactory getInstance() {
        return ef1;
    }
}
