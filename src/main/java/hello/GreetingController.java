package hello;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import hello.hibernate.MyUserInfo;
import hello.hibernate.MyUserRepository;
import org.hibernate.resource.transaction.backend.jta.internal.synchronization.RegisteredSynchronization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class GreetingController {
    private static final String template = "Hello,%s";
    private final AtomicLong counter = new AtomicLong();

    @Autowired
    private MyUserRepository myUserRepository;


    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }

    @RequestMapping("/")
    public String showPage(){
        return "Hello , new springer!";
    }

    @GetMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request){
        String userName = request.getUserName();
        String pwd = request.getPwd();
        List<MyUserInfo> users = myUserRepository.findByUserName(userName);
        if(users!=null && !users.isEmpty()) {
            for (MyUserInfo user : users) {
                if (user.getPwd().equals(pwd)) {
                    System.out.println(user.getUserId() + " " + user.getUserName() + " " + user.getPwd() + "  " + user.getCreateTime());
                    return new LoginResponse("Login succeeded..", LoginResponse.Status.SUCCCEDED);
                }
            }
        } else {
            return new LoginResponse("user doesn't exsit.", LoginResponse.Status.FAILED);
        }

        return new LoginResponse("User and Password not match", LoginResponse.Status.FAILED);

    }

    @PostMapping("/register")
    public RegisterResponse addNewAccounts(@RequestBody RegisterReuest newUser){
       List<MyUserInfo> user = myUserRepository.findByUserName(newUser.getUserName());
       if(user!=null && !user.isEmpty()){
           return new RegisterResponse("user alreay exist", RegisterResponse.Status.FAILED);
       }

       MyUserInfo newU = new MyUserInfo();
       newU.setUserName(newUser.getUserName());
       newU.setPwd(newUser.getPwd());
       newU.setCreateTime(Time.valueOf(LocalTime.now()));

       myUserRepository.save(newU);
       return new RegisterResponse("new acount successfully created.", RegisterResponse.Status.SUCCEEDED);

    }

    @PutMapping("/updatePwd")
    public UpdatePwdResponse updatePwd(@RequestBody UpdatePwdRequest request ){
        String userName = request.getUserName();
        String pwd = request.getPwd();
        List<MyUserInfo> users = myUserRepository.findByUserName(userName);

        if(users!=null && !users.isEmpty()) {
            for (MyUserInfo user : users) {
                user.setPwd(pwd);
                myUserRepository.save(user);
                return new UpdatePwdResponse("user info updated", UpdatePwdResponse.Status.SUCCEEDED);
            }
        }
        return new UpdatePwdResponse("user doesn't exist", UpdatePwdResponse.Status.FAILED);

    }


}
