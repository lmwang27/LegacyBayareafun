package hello;

public class LoginResponse {
    public enum Status {
        SUCCCEDED,
        FAILED
    };

    private String reason ;
    private Status st;

    public LoginResponse(String reason, Status st) {
        this.reason = reason;
        this.st = st;
    }

    public String getReason() {
        return reason;
    }

    public Status getSt() {
        return st;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setSt(Status st) {
        this.st = st;
    }
}
