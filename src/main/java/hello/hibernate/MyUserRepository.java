package hello.hibernate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyUserRepository  extends JpaRepository<MyUserInfo,Integer> {
    List<MyUserInfo> findByUserId(Integer userId);
    List<MyUserInfo> findByUserName(String  userName);
    //boolean deletByUserName(String userName);
}
