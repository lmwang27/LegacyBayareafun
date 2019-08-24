package hello;

public class RegisterResponse {
    public enum Status {
            SUCCEEDED,
            FAILED
    };

    private String result ;
    private Status status;

    public RegisterResponse(String result, Status status) {
        this.result = result;
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
