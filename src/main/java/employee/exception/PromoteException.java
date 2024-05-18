package employee.exception;

public class PromoteException extends Exception{
    private String reason;

    public PromoteException(String s) {
        this.reason = s;
    }

    public String toString() {
        return "Promote Exception: " + this.reason;
    }
}
