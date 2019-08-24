package hello.hibernate;

import javax.persistence.*;
import java.sql.Time;

@Entity
@Table(name = "my_user")
public class MyUserInfo {
    private int  userId ;
    private String userName;
    private String pwd;
    private Time createTime;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "user_id", updatable = false, nullable = false)
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Column(name = "user_name")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Column(name = "pwd")
    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Column(name = "create_time")
    public Time getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Time createTime) {
        this.createTime = createTime;
    }

}
