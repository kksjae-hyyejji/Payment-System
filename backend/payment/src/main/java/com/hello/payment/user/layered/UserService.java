package com.hello.payment.user.layered;

import com.hello.payment.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


    public void regist(String name) {

        User user = User.builder()
                .userName(name)
                .money(10000)
                .orderList(null)
                .build();

        userRepository.save(user);

    }

    public User find(String name) {

        return userRepository.findByUserName(name).orElseThrow();
    }

    public void delete() {

        userRepository.deleteAll();
    }
}
