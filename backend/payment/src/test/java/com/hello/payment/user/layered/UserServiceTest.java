package com.hello.payment.user.layered;

import com.hello.payment.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void regist() {

        String name = "test2";
        userService.regist(name);

        User user = userService.find(name);
        System.out.println(user.getUserId() + " " + user.getUserName());

        assertThat(user.getUserName()).isEqualTo("test2");
        userService.delete();

    }

}