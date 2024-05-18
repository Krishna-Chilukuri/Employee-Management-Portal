package employee.exception;

public class DemoteException extends Exception{
    private String reason;

    public DemoteException(String s) {
        this.reason = s;
    }

    public String toString() {
        return "Demote Exception: " + this.reason;
    }
}
