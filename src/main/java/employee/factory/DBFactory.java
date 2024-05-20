package employee.factory;

public class DBFactory {
    public String url;
    public String username;
    public String password;

    public static DBFactory dbf = new DBFactory();

    private DBFactory() {
        this.url = "jdbc:mysql://localhost:3306/employee_mgmt_portal";
        this.username = "root";
        this.password = "pass123";
    }

    public static DBFactory getInstance() { return dbf; }
}
