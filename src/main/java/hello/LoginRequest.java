package hello;

public class LoginRequest {
    private  String userName;
    private  String pwd;

    public LoginRequest(String userName, String pwd) {
        this.userName = userName;
        this.pwd = pwd;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getUserName() {
        return userName;
    }

    public String getPwd() {
        return pwd;
    }

}
