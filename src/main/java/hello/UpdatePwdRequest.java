package hello;

import java.sql.Time;

public class UpdatePwdRequest {
    private String userName;
    private String pwd;

    public UpdatePwdRequest(int userId, String userName, String pwd, Time createTime) {
        setPwd(pwd);
        setUserName(userName);
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

}
