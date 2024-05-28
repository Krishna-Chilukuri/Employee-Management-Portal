package employee.model;

import java.util.List;

public class Employee {
    private long employeeId;
    private String employeeName;
    private long employeeRank;

    private Employee reportsTo;
    private List<Employee> reportees;

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public long getEmployeeRank() {
        return employeeRank;
    }

    public void setEmployeeRank(long employeeRank) {
        this.employeeRank = employeeRank;
    }

    public Employee getReportsTo() {
        return reportsTo;
    }

    public void setReportsTo(Employee reportsTo) {
        this.reportsTo = reportsTo;
    }


    public List<Employee> getReportees() {
        return reportees;
    }

    public void setReportees(List<Employee> reportees) {
        this.reportees = reportees;
    }

    public void addReportee(Employee reportee) {
        this.reportees.add(reportee);
    }

    public void removeReportee(Employee reportee) {
        this.reportees.remove(reportee);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId = " + employeeId +
                ", employeeName = '" + employeeName + '\'' +
                ", employeeRank = " + employeeRank +
                ", reportsTo = " + reportsTo +
                ", reportees = " + reportees +
                '}';
    }
}
